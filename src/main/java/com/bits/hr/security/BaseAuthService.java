package com.bits.hr.security;

import java.util.Set;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Base Auth Service.
 */
public interface BaseAuthService {
    /**Authorize an authenticated user and it's authorities.*/
    default void authorize(Object principal, Set<GrantedAuthority> authorities) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "", authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
