package com.bits.hr.config;

import com.bits.hr.security.*;
import com.bits.hr.security.jwt.JWTConfigurer;
import com.bits.hr.security.jwt.TokenProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@RequiredArgsConstructor
public abstract class AbstractSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(AbstractSecurityConfiguration.class);
    private final CorsFilter corsFilter;
    private final SecurityProblemSupport problemSupport;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private PermissionManagerService permissionManagerService;

    @Autowired
    private ClientPermissionService clientPermissionService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web
            .ignoring()
            .antMatchers(HttpMethod.OPTIONS, "/**")
            .antMatchers("/app/**/*.{js,html}")
            .antMatchers("/i18n/**")
            .antMatchers("/content/**")
            .antMatchers("/h2-console/**")
            .antMatchers("/swagger-ui/index.html")
            .antMatchers("/test/**")
            .antMatchers("/api/exception-translator-test/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .csrf()
            .ignoringAntMatchers("/h2-console/**")
            .disable()
            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling()
            .authenticationEntryPoint(problemSupport)
            .accessDeniedHandler(problemSupport)
            .and()
            .headers()
            .contentSecurityPolicy("default-src 'self'; frame-src 'self' data:; script-src 'self' 'unsafe-inline' 'unsafe-eval' https://storage.googleapis.com; style-src 'self' https://fonts.googleapis.com 'unsafe-inline'; img-src 'self' data:; font-src 'self' https://fonts.gstatic.com https://fonts.googleapis.com data:")
            .and()
            .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
            .and()
            .permissionsPolicy().policy("camera=(), fullscreen=(self), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr=()")
//            .featurePolicy("geolocation 'none'; midi 'none'; sync-xhr 'none'; microphone 'none'; camera 'none'; magnetometer 'none'; gyroscope 'none'; speaker 'none'; fullscreen 'self'; payment 'none'")
            .and()
            .frameOptions().sameOrigin()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/app/**/*.{js,html}").permitAll()
            .antMatchers("/i18n/**").permitAll()
            .antMatchers("/content/**").permitAll()
            .antMatchers("/swagger-ui/**").permitAll()
            .antMatchers("/test/**").permitAll()
            .antMatchers("/h2-console/**").permitAll()
            .antMatchers("/api/authenticate").permitAll()
            .antMatchers("/api/employeeSearch/**").permitAll()
            .antMatchers("/api/register").permitAll()
            .antMatchers("/api/activate").permitAll()
            .antMatchers("/api/account/reset-password/init").permitAll()
            .antMatchers("/api/account/reset-password/finish").permitAll()
            .antMatchers("/api/account/change-password").permitAll()
            .antMatchers(HttpMethod.GET, "/api/account").authenticated()
            .antMatchers("/api/configs/**").hasAnyAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/api/admin/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/api/authorities/**").hasAnyAuthority(AuthoritiesConstants.ADMIN);
        //            .antMatchers(HttpMethod.GET, "/api/designations/**").authenticated()
        //            .antMatchers("/api/designations/**").hasAnyAuthority(AuthoritiesConstants.ADMIN);
        //
        //            .antMatchers("/api/**").authenticated()

        //            .antMatchers("/management/health").permitAll()
        //            .antMatchers("/management/info").permitAll()
        //            .antMatchers("/management/prometheus").permitAll()
        //            .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
        //            .and()
        //            .httpBasic()
        //            .and()
        //            .apply(securityConfigurerAdapter());
        // @formatter:on

        List<Permission> permissionList = permissionManagerService.getAllPermissions();
        for (Permission permission : permissionList) {
            logger.debug(permission.toString());
            List<String> authorityList = permission.getAuthorities();
            if (authorityList != null && authorityList.size() > 0) {
                String[] authorities = authorityList.toArray(new String[authorityList.size()]);
                if (permission.getRequestMethod() == null || permission.getRequestMethod().isEmpty()) {
                    http.authorizeRequests().antMatchers(permission.getUrl()).hasAnyAuthority(authorities);
                } else {
                    http
                        .authorizeRequests()
                        .antMatchers(HttpMethod.valueOf(permission.getRequestMethod()), permission.getUrl())
                        .hasAnyAuthority(authorities);
                }
            } else {
                if (permission.getRequestMethod() == null || permission.getRequestMethod().isEmpty()) {
                    http.authorizeRequests().antMatchers(permission.getUrl()).authenticated();
                } else {
                    http
                        .authorizeRequests()
                        .antMatchers(HttpMethod.valueOf(permission.getRequestMethod()), permission.getUrl())
                        .authenticated();
                }
            }
        }

        http
            .authorizeRequests()
            .antMatchers("/management/health")
            .permitAll()
            .antMatchers("/management/info")
            .permitAll()
            .antMatchers("/management/prometheus")
            .permitAll()
            .antMatchers("/api/external/**")
            .hasAuthority(AuthoritiesConstants.EXTERNAL_API)
            .antMatchers("/management/**")
            .hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/api/**")
            .denyAll()
            .and()
            .httpBasic()
            .and()
            .apply(securityConfigurerAdapter())
            .and()
            .addFilterBefore(externalAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider);
    }

    private ExternalAuthorizationFilter externalAuthorizationFilter() {
        return new ExternalAuthorizationFilter(clientPermissionService);
    }
}
