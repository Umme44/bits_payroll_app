package com.bits.hr.config.ldap;

import com.bits.hr.repository.EmployeeRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Log4j2
public class LdapAuthenticationManager extends LdapAuthenticationProvider {

    @Autowired
    EmployeeRepository employeeRepository;

    public LdapAuthenticationManager(LdapAuthenticator authenticator, LdapAuthoritiesPopulator authoritiesPopulator) {
        super(authenticator, authoritiesPopulator);
    }

    public LdapAuthenticationManager(LdapAuthenticator authenticator) {
        super(authenticator);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(
            UsernamePasswordAuthenticationToken.class,
            authentication,
            () ->
                this.messages.getMessage("LdapAuthenticationProvider.onlySupports", "Only UsernamePasswordAuthenticationToken is supported")
        );

        final UsernamePasswordAuthenticationToken userToken = (UsernamePasswordAuthenticationToken) authentication;

        String username = userToken.getName();
        String password = (String) authentication.getCredentials();

        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Processing authentication request for user: " + username);
        }

        if (!StringUtils.hasLength(username)) {
            throw new BadCredentialsException(this.messages.getMessage("LdapAuthenticationProvider.emptyUsername", "Empty Username"));
        }

        if (!StringUtils.hasLength(password)) {
            throw new BadCredentialsException(
                this.messages.getMessage("AbstractLdapAuthenticationProvider.emptyPassword", "Empty Password")
            );
        }

        Assert.notNull(password, "Null password was supplied in authentication token");

        DirContextOperations userData = doAuthentication(userToken);

        UserDetails user =
            this.userDetailsContextMapper.mapUserFromContext(
                    userData,
                    authentication.getName(),
                    loadUserAuthorities(userData, authentication.getName(), (String) authentication.getCredentials())
                );

        return createCustomLdapAuthentication(userToken, user, userData);
    }

    private Authentication createCustomLdapAuthentication(
        UsernamePasswordAuthenticationToken userToken,
        UserDetails user,
        DirContextOperations userData
    ) {
        CustomLdapUserDetails customLdapUserDetails = new CustomLdapUserDetails();
        customLdapUserDetails.setDn(userData.getNameInNamespace());
        customLdapUserDetails.setManager(getAttribute(userData, "manager"));
        customLdapUserDetails.setAuthorities(user.getAuthorities());
        customLdapUserDetails.setCompany(getAttribute(userData, "company"));
        customLdapUserDetails.setDepartment(getAttribute(userData, "department"));
        customLdapUserDetails.setCountryCode(getAttribute(userData, "countryCode"));
        customLdapUserDetails.setEmail(eliminateWhitespaces(getAttribute(userData, "mail")));
        customLdapUserDetails.setDisplayName(getAttribute(userData, "displayName"));
        customLdapUserDetails.setGivenName(getAttribute(userData, "givenName"));
        customLdapUserDetails.setName(getAttribute(userData, "name"));
        customLdapUserDetails.setPin(getPin(userData));
        customLdapUserDetails.setTitle(getAttribute(userData, "title"));
        customLdapUserDetails.setTelephoneNumber(getAttribute(userData, "telephoneNumber"));
        customLdapUserDetails.setPrimaryGroupID(getAttribute(userData, "primaryGroupID"));
        customLdapUserDetails.setSAMAccountName(getAttribute(userData, "samaccountname"));
        customLdapUserDetails.setSAMAccountType(getAttribute(userData, "samaccounttype"));
        customLdapUserDetails.setUserPrincipalName(getAttribute(userData, "userPrincipalName"));

        log.info("customLdapUserDetails================================================");
        log.info(customLdapUserDetails.toString());
        log.info("userData================================================");
        log.info(userData.toString());
        return new UsernamePasswordAuthenticationToken(customLdapUserDetails, user, user.getAuthorities());
    }

    private String getAttribute(DirContextOperations userData, String key) {
        try {
            return userData.getAttributes().get(key).get().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String eliminateWhitespaces(String text) {
        return text.trim().replace(" ", "");
    }

    private String getPin(DirContextOperations userData) {
        String pin = getAttribute(userData, "sn");
        if (pin == null || pin.isEmpty()) {
            // take pin from internal mapping
            log.info("pin not given from ldap..");
            String officialEmail = getAttribute(userData, "mail");
            return employeeRepository.getPinByOfficialEmail(officialEmail);
        } else {
            return pin;
        }
    }

    @Override
    protected Authentication createSuccessfulAuthentication(UsernamePasswordAuthenticationToken authentication, UserDetails user) {
        return super.createSuccessfulAuthentication(authentication, user);
    }
}
