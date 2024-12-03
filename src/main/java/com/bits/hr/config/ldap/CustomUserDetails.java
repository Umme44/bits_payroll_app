package com.bits.hr.config.ldap;

import java.util.Collection;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@SuppressWarnings("unchecked")
@Getter
@Setter
public class CustomUserDetails implements UserDetails {

    private Long userId;
    private String email;
    private String pin;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public void setEmail(String email) {
        this.email = eliminateWhitespaces(email);
    }

    private String eliminateWhitespaces(String text) {
        try {
            return text.trim().replace(" ", "");
        } catch (Exception e) {
            return text;
        }
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return (Collection<GrantedAuthority>) authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = eliminateWhitespaces(username);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return (
            "CustomUserDetails{" +
            "userId=" +
            userId +
            ", email='" +
            email +
            '\'' +
            ", pin='" +
            pin +
            '\'' +
            ", username='" +
            username +
            '\'' +
            ", password='" +
            password +
            '\'' +
            ", authorities=" +
            authorities +
            '}'
        );
    }
}
