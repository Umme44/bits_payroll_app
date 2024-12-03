package com.bits.hr.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class ExternalAuthorizationFilter extends OncePerRequestFilter {

    public static final String EXTERNAL_CLIENT_AUTH_HEADER = "X-Client-Id";
    private final ClientPermissionService clientPermissionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        String clientId = request.getHeader(EXTERNAL_CLIENT_AUTH_HEADER);
        if (clientId != null && !clientId.isEmpty()) {
            clientPermissionService.authorizeClient(clientId);
        }
        filterChain.doFilter(request, response);
    }
}
