package com.bits.hr.config;

import com.bits.hr.config.ldap.CustomLdapAuthenticationProviderConfigurer;
import com.bits.hr.security.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Profile("ldap")
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import({ SecurityProblemSupport.class, TokenProvider.class })
public class SecurityConfiguration extends AbstractSecurityConfiguration {

    @Value("${config.ldap.search-filter.user}")
    private String ldapUserSearchFilter;

    @Value("${config.ldap.search-base.user}")
    private String ldapUserSearchBase;

    @Value("${config.ldap.search-base.group}")
    private String ldapGroupSearchBase;

    @Value("${config.ldap.search-filter.group}")
    private String ldapGroupSearchFilter;

    @Value("${config.ldap.url}")
    private String ldapUrl;

    @Value("${config.ldap.port}")
    private int ldapPort;

    @Value("${config.ldap.manager-dn}")
    private String ldapManagerDn;

    @Value("${config.ldap.manager-password}")
    private String ldapManagerPassword;

    @Autowired
    private UserDetailsService userDetailsService;

    public SecurityConfiguration(CorsFilter corsFilter, SecurityProblemSupport problemSupport) {
        super(corsFilter, problemSupport);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder())
            .and()
            .apply(new CustomLdapAuthenticationProviderConfigurer<>())
            .userSearchFilter(ldapUserSearchFilter)
            .userSearchBase(ldapUserSearchBase)
            .groupSearchBase(ldapGroupSearchBase)
            .groupSearchFilter(ldapGroupSearchFilter)
            .contextSource()
            .url(ldapUrl)
            .port(ldapPort)
            .managerDn(ldapManagerDn)
            .managerPassword(ldapManagerPassword);
    }
}
