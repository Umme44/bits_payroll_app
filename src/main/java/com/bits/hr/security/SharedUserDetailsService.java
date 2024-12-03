package com.bits.hr.security;

import com.bits.hr.config.Constants;
import com.bits.hr.config.ldap.CustomUserDetails;
import com.bits.hr.domain.Authority;
import com.bits.hr.domain.User;
import com.bits.hr.repository.AuthorityRepository;
import com.bits.hr.repository.UserRepository;
import com.bits.hr.service.dto.UserDTO;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tech.jhipster.security.RandomUtil;

/**
 * Authenticate a user from the database.
 */
/*FIXME: Merge DomainUserDetailsService : SharedUserDetailsService */
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Component
public class SharedUserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthorityRepository authorityRepository;

    private org.springframework.security.core.userdetails.UserDetails createSpringSecurityUser(String lowercaseLogin, User user) {
        if (!user.isActivated()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
        }
        List<String> authorityList = user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toList());

        List<GrantedAuthority> grantedAuthorities = authorityList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setAuthorities(grantedAuthorities);
        customUserDetails.setUsername(user.getLogin());
        customUserDetails.setEmail(user.getEmail());
        customUserDetails.setUserId(user.getId());
        customUserDetails.setPassword(user.getPassword());
        return customUserDetails;
    }

    /**
     * Mainly used, while an user logged in by LDAP.
     */
    public UserDetails createOrLoadUserDetails() {
        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(SecurityException::new).toLowerCase();
        return createSpringSecurityUser(
            userLogin,
            userRepository
                .findOneWithAuthoritiesByLogin(userLogin)
                .orElseGet(() ->
                    userRepository
                        .findOneWithAuthoritiesByEmailIgnoreCase(userLogin)
                        .orElseGet(() -> createUser(new UserDTO(userLogin, AuthoritiesConstants.EMPLOYEE, AuthoritiesConstants.USER)))
                )
        );
    }

    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO
                .getAuthorities()
                .stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        userRepository.save(user);
        userRepository.flush();
        return user;
    }
}
