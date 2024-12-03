package com.bits.hr.security.jwt;

import com.bits.hr.config.ldap.CustomLdapUserDetails;
import com.bits.hr.config.ldap.CustomUserDetails;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.UserRepository;
import com.bits.hr.security.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import tech.jhipster.config.JHipsterProperties;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";
    private static final String PIN_KEY = "pin";
    private static final String UID_KEY = "uid";
    private static final String EMAIL_KEY = "email";
    private final JHipsterProperties jHipsterProperties;
    private Key key;
    private long tokenValidityInMilliseconds;
    private long tokenValidityInMillisecondsForRememberMe;

    @Lazy
    @Autowired
    private DomainUserDetailsService domainUserDetailsService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    //    private final JwtParser jwtParser;
    //    private final SecurityMetersService securityMetersService
    @PostConstruct
    public void init() {
        byte[] keyBytes;
        String secret = jHipsterProperties.getSecurity().getAuthentication().getJwt().getSecret();
        if (!StringUtils.isEmpty(secret)) {
            log.warn(
                "Warning: the JWT key used is not Base64-encoded. " +
                "We recommend using the `jhipster.security.authentication.jwt.base64-secret` key for optimum security."
            );
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        } else {
            log.debug("Using a Base64-encoded JWT secret key");
            keyBytes = Decoders.BASE64.decode(jHipsterProperties.getSecurity().getAuthentication().getJwt().getBase64Secret());
        }
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.tokenValidityInMilliseconds = 1000 * jHipsterProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds();
        this.tokenValidityInMillisecondsForRememberMe =
            1000 * jHipsterProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSecondsForRememberMe();
    }

    /*TODO: Fix, while user login by ldap.*/
    public String createToken(Authentication authentication, boolean rememberMe) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        if (principal instanceof CustomLdapUserDetails) {
            final UserDetails userDetails = domainUserDetailsService.createOrLoadUserDetails();
            principal.setUserId(((CustomUserDetails) userDetails).getUserId());
            principal.setAuthorities(userDetails.getAuthorities());
        } else {
            principal.setPin(employeeRepository.findEmployeePinByUserId(((CustomUserDetails) principal).getUserId()).orElse(null));
        }

        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }

        return Jwts
            .builder()
            .setSubject(authentication.getName())
            .claim(
                AUTHORITIES_KEY,
                principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","))
            )
            .claim(PIN_KEY, principal.getPin())
            .claim(EMAIL_KEY, principal.getEmail())
            .claim(UID_KEY, principal.getUserId())
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(validity)
            .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

        Collection<? extends GrantedAuthority> authorities = Arrays
            .stream(claims.get(AUTHORITIES_KEY).toString().split(","))
            .filter(AUTHORITIES_KEY -> (AUTHORITIES_KEY) != null && !(AUTHORITIES_KEY).isEmpty())
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        CustomUserDetails principal = new CustomUserDetails();
        principal.setUsername(claims.getSubject());
        principal.setEmail(Optional.ofNullable(claims.get(EMAIL_KEY)).map(Object::toString).orElse(null));
        principal.setUserId(Optional.ofNullable(claims.get(UID_KEY)).map(Object::toString).map(Long::parseLong).orElse(null));
        principal.setPin(Optional.ofNullable(claims.get(PIN_KEY)).map(Object::toString).orElse(null));
        principal.setAuthorities(authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace.", e);
        }
        return false;
    }
}
