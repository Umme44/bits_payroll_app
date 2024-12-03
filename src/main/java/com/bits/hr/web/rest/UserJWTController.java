package com.bits.hr.web.rest;

import com.bits.hr.config.ldap.CustomLdapUserDetails;
import com.bits.hr.security.jwt.JWTFilter;
import com.bits.hr.security.jwt.TokenProvider;
import com.bits.hr.service.FailedLoginAttemptService;
import com.bits.hr.service.UserService;
import com.bits.hr.service.config.GetConfigValueByKeyService;
import com.bits.hr.web.rest.vm.LoginVM;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Locale;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserJWTController {

    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final GetConfigValueByKeyService getConfigValueByKeyService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final FailedLoginAttemptService failedLoginAttemptService;

    @PostMapping("/authenticate")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {
        failedLoginAttemptService.validateFailedAttemptIfAny(loginVM.getUsername());

        loginVM = loginUserNameProcess(loginVM);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginVM.getUsername(),
            loginVM.getPassword()
        );
        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            failedLoginAttemptService.resetFailedAttemptIfPresent(loginVM.getUsername());

            if (authentication.getPrincipal() instanceof CustomLdapUserDetails) {
                userService.createUserIfNotExists((CustomLdapUserDetails) authentication.getPrincipal());
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
            boolean rememberMe = loginVM.isRememberMe() != null && loginVM.isRememberMe();
            String jwt = tokenProvider.createToken(authentication, rememberMe);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
            return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            failedLoginAttemptService.recordIncidentForBadCredentials(loginVM.getUsername());
            throw e;
        }
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }

    private LoginVM loginUserNameProcess(LoginVM loginVM) {
        String userName = loginVM.getUsername().toLowerCase(Locale.ROOT);

        if (userName.contains("@")) {
            String[] parts = userName.split("@");

            String domain = getConfigValueByKeyService.getDomainName();

            if (parts[1].contains(domain)) {
                userName = parts[0];
                loginVM.setUsername(userName);
            }
        }
        return loginVM;
    }
}
