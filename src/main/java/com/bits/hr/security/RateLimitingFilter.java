package com.bits.hr.security;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Profile("enable-rate-limiting")
@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private CacheManager cacheManager;
    private final long capacity = 10; // maximum number of requests
    private final Duration refillPeriod = Duration.of(1, ChronoUnit.MINUTES); // refill the bucket every 1 minute
    private final long refillTokens = 10; // number of tokens to refill

    //    public RateLimitingFilter(CacheManager cacheManager) {
    //        this.cacheManager = cacheManager;
    //    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        String jwtToken = request.getHeader("Authorization");

        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            String key = "rate-limit:" + jwtToken;

            Cache<String, Long> cache = cacheManager.getCache("rateLimitCache", String.class, Long.class);

            Long tokens = cache.get(key);
            if (tokens == null) {
                tokens = refillTokens;
                cache.put(key, tokens);
            }

            if (tokens > 0) {
                // Request within the limit
                tokens--;
                cache.put(key, tokens);
                filterChain.doFilter(request, response);
            } else {
                // Rate limit exceeded
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("Rate limit exceeded");
                response.getWriter().flush();
            }
        } else {
            // No JWT token found in the header
            filterChain.doFilter(request, response);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Unauthorized - JWT token missing");
            response.getWriter().flush();
        }
    }
}
