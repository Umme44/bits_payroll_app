package com.bits.hr.config.ldap;

import java.io.IOException;
import java.net.ServerSocket;
import lombok.extern.log4j.Log4j2;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.ProviderManagerBuilder;
import org.springframework.security.config.annotation.web.configurers.ChannelSecurityConfigurer;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.*;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.search.LdapUserSearch;
import org.springframework.security.ldap.server.ApacheDSContainer;
import org.springframework.security.ldap.server.UnboundIdContainer;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

@Component
@Log4j2
public class CustomLdapAuthenticationProviderConfigurer<B extends ProviderManagerBuilder<B>>
    extends SecurityConfigurerAdapter<AuthenticationManager, B> {

    private String groupRoleAttribute = "cn";
    private String groupSearchBase = "";
    private String groupSearchFilter = "(uniqueMember={0})";
    private String rolePrefix = "ROLE_";
    private String userSearchBase = ""; // only for search
    private String userSearchFilter = null; // "uid={0}"; // only for search
    private String[] userDnPatterns;
    private BaseLdapPathContextSource contextSource;
    private final ContextSourceBuilder contextSourceBuilder = new ContextSourceBuilder();
    private UserDetailsContextMapper userDetailsContextMapper;
    private PasswordEncoder passwordEncoder;
    private String passwordAttribute;
    private LdapAuthoritiesPopulator ldapAuthoritiesPopulator;
    private GrantedAuthoritiesMapper authoritiesMapper;

    /**
     * Specifies the {@link LdapAuthoritiesPopulator}.
     *
     * @param ldapAuthoritiesPopulator the {@link LdapAuthoritiesPopulator} the default is
     *                                 {@link DefaultLdapAuthoritiesPopulator}
     * @return the CustomLdapAuthenticationProviderConfigurer<B> for further customizations
     */
    public CustomLdapAuthenticationProviderConfigurer<B> ldapAuthoritiesPopulator(LdapAuthoritiesPopulator ldapAuthoritiesPopulator) {
        this.ldapAuthoritiesPopulator = ldapAuthoritiesPopulator;
        return this;
    }

    /**
     * Adds an {@link ObjectPostProcessor} for this class.
     *
     * @param objectPostProcessor
     * @return the {@link ChannelSecurityConfigurer} for further customizations
     */
    public CustomLdapAuthenticationProviderConfigurer<B> withObjectPostProcessor(ObjectPostProcessor<?> objectPostProcessor) {
        addObjectPostProcessor(objectPostProcessor);
        return this;
    }

    public CustomLdapAuthenticationProviderConfigurer<B> authoritiesMapper(GrantedAuthoritiesMapper grantedAuthoritiesMapper) {
        this.authoritiesMapper = grantedAuthoritiesMapper;
        return this;
    }

    public CustomLdapAuthenticationProviderConfigurer<B> contextSource(BaseLdapPathContextSource contextSource) {
        this.contextSource = contextSource;
        return this;
    }

    public ContextSourceBuilder contextSource() {
        return contextSourceBuilder;
    }

    public CustomLdapAuthenticationProviderConfigurer<B> passwordEncoder(
        final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder
    ) {
        Assert.notNull(passwordEncoder, "passwordEncoder must not be null.");
        this.passwordEncoder = passwordEncoder;
        return this;
    }

    public CustomLdapAuthenticationProviderConfigurer<B> userDnPatterns(String... userDnPatterns) {
        this.userDnPatterns = userDnPatterns;
        return this;
    }

    public CustomLdapAuthenticationProviderConfigurer<B> userDetailsContextMapper(UserDetailsContextMapper userDetailsContextMapper) {
        this.userDetailsContextMapper = userDetailsContextMapper;
        return this;
    }

    public CustomLdapAuthenticationProviderConfigurer<B> groupRoleAttribute(String groupRoleAttribute) {
        this.groupRoleAttribute = groupRoleAttribute;
        return this;
    }

    public CustomLdapAuthenticationProviderConfigurer<B> groupSearchBase(String groupSearchBase) {
        this.groupSearchBase = groupSearchBase;
        return this;
    }

    public CustomLdapAuthenticationProviderConfigurer<B> groupSearchFilter(String groupSearchFilter) {
        this.groupSearchFilter = groupSearchFilter;
        return this;
    }

    public CustomLdapAuthenticationProviderConfigurer<B> rolePrefix(String rolePrefix) {
        this.rolePrefix = rolePrefix;
        return this;
    }

    public CustomLdapAuthenticationProviderConfigurer<B> userSearchBase(String userSearchBase) {
        this.userSearchBase = userSearchBase;
        return this;
    }

    public CustomLdapAuthenticationProviderConfigurer<B> userSearchFilter(String userSearchFilter) {
        this.userSearchFilter = userSearchFilter;
        return this;
    }

    @Override
    public void configure(B builder) throws Exception {
        LdapAuthenticationProvider provider = postProcess(build());
        builder.authenticationProvider(provider);
    }

    private LdapAuthenticationProvider build() throws Exception {
        BaseLdapPathContextSource contextSource = getContextSource();
        LdapAuthenticator ldapAuthenticator = createLdapAuthenticator(contextSource);

        LdapAuthoritiesPopulator authoritiesPopulator = getLdapAuthoritiesPopulator();

        LdapAuthenticationProvider ldapAuthenticationProvider = new LdapAuthenticationManager(ldapAuthenticator, authoritiesPopulator);
        ldapAuthenticationProvider.setAuthoritiesMapper(getAuthoritiesMapper());
        if (userDetailsContextMapper != null) {
            ldapAuthenticationProvider.setUserDetailsContextMapper(userDetailsContextMapper);
        }
        return ldapAuthenticationProvider;
    }

    private BaseLdapPathContextSource getContextSource() throws Exception {
        if (contextSource == null) {
            contextSource = contextSourceBuilder.build();
        }
        return contextSource;
    }

    private LdapAuthenticator createLdapAuthenticator(BaseLdapPathContextSource contextSource) {
        AbstractLdapAuthenticator ldapAuthenticator = passwordEncoder == null
            ? createBindAuthenticator(contextSource)
            : createPasswordCompareAuthenticator(contextSource);
        LdapUserSearch userSearch = createUserSearch();
        if (userSearch != null) {
            ldapAuthenticator.setUserSearch(userSearch);
        }
        if (userDnPatterns != null && userDnPatterns.length > 0) {
            ldapAuthenticator.setUserDnPatterns(userDnPatterns);
        }
        return postProcess(ldapAuthenticator);
    }

    /**
     * Gets the {@link LdapAuthoritiesPopulator} and defaults to
     * {@link DefaultLdapAuthoritiesPopulator}
     *
     * @return the {@link LdapAuthoritiesPopulator}
     */
    private LdapAuthoritiesPopulator getLdapAuthoritiesPopulator() {
        if (ldapAuthoritiesPopulator != null) {
            return ldapAuthoritiesPopulator;
        }

        DefaultLdapAuthoritiesPopulator defaultAuthoritiesPopulator = new DefaultLdapAuthoritiesPopulator(contextSource, groupSearchBase);
        defaultAuthoritiesPopulator.setGroupRoleAttribute(groupRoleAttribute);
        defaultAuthoritiesPopulator.setGroupSearchFilter(groupSearchFilter);
        defaultAuthoritiesPopulator.setRolePrefix(this.rolePrefix);

        this.ldapAuthoritiesPopulator = defaultAuthoritiesPopulator;
        return defaultAuthoritiesPopulator;
    }

    protected GrantedAuthoritiesMapper getAuthoritiesMapper() throws Exception {
        if (authoritiesMapper != null) {
            return authoritiesMapper;
        }

        SimpleAuthorityMapper simpleAuthorityMapper = new SimpleAuthorityMapper();
        simpleAuthorityMapper.setPrefix(this.rolePrefix);
        simpleAuthorityMapper.afterPropertiesSet();
        this.authoritiesMapper = simpleAuthorityMapper;
        return simpleAuthorityMapper;
    }

    private BindAuthenticator createBindAuthenticator(BaseLdapPathContextSource contextSource) {
        return new BindAuthenticator(contextSource);
    }

    private PasswordComparisonAuthenticator createPasswordCompareAuthenticator(BaseLdapPathContextSource contextSource) {
        PasswordComparisonAuthenticator ldapAuthenticator = new PasswordComparisonAuthenticator(contextSource);
        if (passwordAttribute != null) {
            ldapAuthenticator.setPasswordAttributeName(passwordAttribute);
        }
        ldapAuthenticator.setPasswordEncoder(passwordEncoder);
        return ldapAuthenticator;
    }

    private LdapUserSearch createUserSearch() {
        if (userSearchFilter == null) {
            return null;
        }
        return new FilterBasedLdapUserSearch(userSearchBase, userSearchFilter, contextSource);
    }

    /**
     * @return the {@link org.springframework.security.config.annotation.authentication.configurers.ldap.LdapAuthenticationProviderConfigurer.PasswordCompareConfigurer} for further customizations
     */
    public CustomLdapAuthenticationProviderConfigurer.PasswordCompareConfigurer passwordCompare() {
        return new CustomLdapAuthenticationProviderConfigurer.PasswordCompareConfigurer()
            .passwordAttribute("password")
            .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    /**
     * Sets up Password based comparison
     *
     * @author Rob Winch
     */
    public final class PasswordCompareConfigurer {

        private PasswordCompareConfigurer() {}

        /**
         * Us
         * Allows specifying the {@link PasswordEncoder} to use. The default is
         * {@link org.springframework.security.crypto.password.NoOpPasswordEncoder}.
         *
         * @param passwordEncoder the {@link PasswordEncoder} to use
         * @return the {@link org.springframework.security.config.annotation.authentication.configurers.ldap.LdapAuthenticationProviderConfigurer.PasswordCompareConfigurer} for further customizations
         */
        public CustomLdapAuthenticationProviderConfigurer.PasswordCompareConfigurer passwordEncoder(PasswordEncoder passwordEncoder) {
            CustomLdapAuthenticationProviderConfigurer.this.passwordEncoder = passwordEncoder;
            return this;
        }

        /**
         * The attribute in the directory which contains the user password. Defaults to
         * "userPassword".
         *
         * @param passwordAttribute the attribute in the directory which contains the user
         *                          password
         * @return the
         */
        public PasswordCompareConfigurer passwordAttribute(String passwordAttribute) {
            CustomLdapAuthenticationProviderConfigurer.this.passwordAttribute = passwordAttribute;
            return this;
        }

        /**
         * Allows obtaining a reference to the
         * CustomLdapAuthenticationProviderConfigurer<B> for further customizations
         *
         * @return attribute in the directory which contains the user password
         */
        public CustomLdapAuthenticationProviderConfigurer<B> and() {
            return CustomLdapAuthenticationProviderConfigurer.this;
        }
    }

    /**
     * Allows building a {@link BaseLdapPathContextSource} and optionally creating an
     * embedded LDAP instance.
     *
     * @author Rob Winch
     * @since 3.2
     */
    public final class ContextSourceBuilder {

        private static final int DEFAULT_PORT = 33389;
        private String ldif = "classpath*:*.ldif";
        private String managerPassword;
        private String managerDn;
        private Integer port;
        private String root = "dc=springframework,dc=org";
        private String url;

        private ContextSourceBuilder() {}

        /**
         * Specifies an ldif to load at startup for an embedded LDAP server. This only
         * loads if using an embedded instance. The default is "classpath*:*.ldif".
         *
         * @param ldif the ldif to load at startup for an embedded LDAP server.
         * @return the
         */
        public ContextSourceBuilder ldif(String ldif) {
            this.ldif = ldif;
            return this;
        }

        /**
         * Username (DN) of the "manager" user identity (i.e. "uid=admin,ou=system") which
         * will be used to authenticate to a (non-embedded) LDAP server. If omitted,
         * anonymous access will be used.
         *
         * @param managerDn the username (DN) of the "manager" user identity used to
         *                  authenticate to a LDAP server.
         * @return the {@link org.springframework.security.config.annotation.authentication.configurers.ldap.LdapAuthenticationProviderConfigurer.ContextSourceBuilder} for further customization
         */
        public ContextSourceBuilder managerDn(String managerDn) {
            this.managerDn = managerDn;
            return this;
        }

        /**
         * The password for the manager DN. This is required if the manager-dn is
         * specified.
         *
         * @param managerPassword password for the manager DN
         * @return the
         */
        public ContextSourceBuilder managerPassword(String managerPassword) {
            this.managerPassword = managerPassword;
            return this;
        }

        /**
         * The port to connect to LDAP to (the default is 33389 or random available port
         * if unavailable).
         *
         * @param port the port to connect to
         * @return the
         */
        public ContextSourceBuilder port(int port) {
            this.port = port;
            return this;
        }

        /**
         * Optional root suffix for the embedded LDAP server. Default is
         * "dc=springframework,dc=org"
         *
         * @param root root suffix for the embedded LDAP server
         * @return the
         */
        public ContextSourceBuilder root(String root) {
            this.root = root;
            return this;
        }

        public ContextSourceBuilder url(String url) {
            this.url = url;
            return this;
        }

        /**
         * Gets the CustomLdapAuthenticationProviderConfigurer<B> for further
         * customizations
         *
         * @return the CustomLdapAuthenticationProviderConfigurer<B> for further
         * customizations
         */
        public CustomLdapAuthenticationProviderConfigurer<B> and() {
            return CustomLdapAuthenticationProviderConfigurer.this;
        }

        private DefaultSpringSecurityContextSource build() throws Exception {
            DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(getProviderUrl());
            if (managerDn != null) {
                contextSource.setUserDn(managerDn);
                if (managerPassword == null) {
                    throw new IllegalStateException("managerPassword is required if managerDn is supplied");
                }
                contextSource.setPassword(managerPassword);
            }
            contextSource = postProcess(contextSource);
            if (url != null) {
                return contextSource;
            }
            if (ClassUtils.isPresent("org.apache.directory.server.core.DefaultDirectoryService", getClass().getClassLoader())) {
                ApacheDSContainer apacheDsContainer = new ApacheDSContainer(root, ldif);
                apacheDsContainer.setPort(getPort());
                postProcess(apacheDsContainer);
            } else if (ClassUtils.isPresent("com.unboundid.ldap.listener.InMemoryDirectoryServer", getClass().getClassLoader())) {
                UnboundIdContainer unboundIdContainer = new UnboundIdContainer(root, ldif);
                unboundIdContainer.setPort(getPort());
                postProcess(unboundIdContainer);
            }
            return contextSource;
        }

        private int getPort() {
            if (port == null) {
                port = getDefaultPort();
            }
            return port;
        }

        private int getDefaultPort() {
            ServerSocket serverSocket = null;
            try {
                try {
                    serverSocket = new ServerSocket(DEFAULT_PORT);
                } catch (IOException e) {
                    try {
                        serverSocket = new ServerSocket(0);
                    } catch (IOException e2) {
                        return DEFAULT_PORT;
                    }
                }
                return serverSocket.getLocalPort();
            } finally {
                if (serverSocket != null) {
                    try {
                        serverSocket.close();
                    } catch (IOException e) {}
                }
            }
        }

        private String getProviderUrl() {
            if (url == null) {
                return "ldap://127.0.0.1:" + getPort() + "/" + root;
            }
            return url;
        }
    }
}
