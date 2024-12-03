package com.bits.hr.config.ldap;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomLdapUserDetails extends CustomUserDetails {

    private String telephoneNumber;
    private String countryCode;
    private String title;
    private String primaryGroupID;
    private String givenName;
    private String company;
    private String department;
    private String manager;
    private String sAMAccountName;
    private String sAMAccountType;
    private String userPrincipalName;
    private String displayName;
    private String name;
    private String dn;

    public void setSAMAccountName(String sAMAccountName) {
        this.sAMAccountName = sAMAccountName;
        this.setUsername(sAMAccountName);
    }

    @Override
    public String toString() {
        return (
            "CustomLdapUserDetails{" +
            "telephoneNumber='" +
            telephoneNumber +
            '\'' +
            ", countryCode='" +
            countryCode +
            '\'' +
            ", title='" +
            title +
            '\'' +
            ", primaryGroupID='" +
            primaryGroupID +
            '\'' +
            ", givenName='" +
            givenName +
            '\'' +
            ", company='" +
            company +
            '\'' +
            ", department='" +
            department +
            '\'' +
            ", manager='" +
            manager +
            '\'' +
            ", sAMAccountName='" +
            sAMAccountName +
            '\'' +
            ", sAMAccountType='" +
            sAMAccountType +
            '\'' +
            ", userPrincipalName='" +
            userPrincipalName +
            '\'' +
            ", displayName='" +
            displayName +
            '\'' +
            ", name='" +
            name +
            '\'' +
            ", dn='" +
            dn +
            '\'' +
            '}'
        );
    }
}
