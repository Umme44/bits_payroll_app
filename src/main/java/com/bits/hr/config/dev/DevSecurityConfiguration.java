package com.bits.hr.config.dev;

import com.bits.hr.config.AbstractSecurityConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@Profile("!ldap")
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
public class DevSecurityConfiguration extends AbstractSecurityConfiguration {

    public DevSecurityConfiguration(CorsFilter corsFilter, SecurityProblemSupport problemSupport) {
        super(corsFilter, problemSupport);
    }
}
