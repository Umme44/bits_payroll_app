package com.bits.hr.security;

import com.bits.hr.config.ldap.CustomUserDetails;
import com.bits.hr.domain.User;
import com.bits.hr.repository.UserRepository;
import com.bits.hr.service.AuthorityMapperService;
import com.bits.hr.service.UserService;
import com.bits.hr.service.dto.UserDTO;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Authenticate a user from the database.
 */
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityMapperService authorityMapperService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);

        if (new EmailValidator().isValid(login, null)) {
            return userRepository
                .findOneWithAuthoritiesByEmailIgnoreCase(login)
                .map(user -> createSpringSecurityUser(login, user))
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + login + " was not found in the database"));
        }

        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
        return userRepository
            .findOneWithAuthoritiesByLogin(lowercaseLogin)
            .map(user -> createSpringSecurityUser(lowercaseLogin, user))
            .orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database"));
    }

    private org.springframework.security.core.userdetails.UserDetails createSpringSecurityUser(String lowercaseLogin, User user) {
        if (!user.isActivated()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
        }
        List<String> authorityList = authorityMapperService.getAuthorities(user.getAuthorities());

        List<GrantedAuthority> grantedAuthorities = authorityList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setAuthorities(grantedAuthorities);
        customUserDetails.setUsername(user.getLogin());
        customUserDetails.setEmail(user.getEmail());
        customUserDetails.setUserId(user.getId());
        customUserDetails.setPassword(user.getPassword());
        return customUserDetails;
    }

    /**
     * Mainly used, while an user logged in by LDAP.
     */
    public UserDetails createOrLoadUserDetails() {
        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(SecurityException::new).toLowerCase();
        return createSpringSecurityUser(
            userLogin,
            userRepository
                .findOneWithAuthoritiesByLogin(userLogin)
                .orElseGet(() ->
                    userRepository
                        .findOneWithAuthoritiesByEmailIgnoreCase(userLogin)
                        .orElseGet(() ->
                            userService.createUser(new UserDTO(userLogin, AuthoritiesConstants.EMPLOYEE, AuthoritiesConstants.USER))
                        )
                )
        );
    }
}
