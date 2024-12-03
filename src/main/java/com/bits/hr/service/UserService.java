package com.bits.hr.service;

import com.bits.hr.config.Constants;
import com.bits.hr.config.ldap.CustomLdapUserDetails;
import com.bits.hr.domain.Authority;
import com.bits.hr.domain.User;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.AuthorityRepository;
import com.bits.hr.repository.UserRepository;
import com.bits.hr.security.AuthoritiesConstants;
import com.bits.hr.security.SecurityUtils;
import com.bits.hr.service.dto.AdminUserDTO;
import com.bits.hr.service.dto.UserDTO;
import com.bits.hr.service.event.EmployeeRegistrationEvent;
import com.bits.hr.service.search.UserFilterDto;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.security.RandomUtil;

/**
 * Service class for managing users.
 */
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityMapperService authorityMapperService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository
            .findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                this.clearUserCaches(user);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository
            .findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                this.clearUserCaches(user);
                return user;
            });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository
            .findOneByEmailIgnoreCase(mail)
            .filter(User::isActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                this.clearUserCaches(user);
                return user;
            });
    }

    public User registerUser(UserDTO userDTO, String password) {
        userRepository
            .findOneByLogin(userDTO.getLogin().toLowerCase())
            .ifPresent(existingUser -> {
                boolean removed = removeNonActivatedUser(existingUser);
                if (!removed) {
                    throw new UsernameAlreadyUsedException();
                }
            });
        userRepository
            .findOneByEmailIgnoreCase(userDTO.getEmail())
            .ifPresent(existingUser -> {
                boolean removed = removeNonActivatedUser(existingUser);
                if (!removed) {
                    throw new EmailAlreadyUsedException();
                }
            });
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            newUser.setEmail(userDTO.getEmail().toLowerCase().trim());
        }
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);

        log.info("================================ user to be saved====================================");
        log.info(newUser.toString());

        userRepository.save(newUser);

        log.info("================================ saved ====================================");

        this.clearUserCaches(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.isActivated()) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        this.clearUserCaches(existingUser);
        return true;
    }

    public User createUser(AdminUserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase().trim());
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

        log.info("================================ user to be saved====================================");
        log.info(user.toString());

        userRepository.save(user);

        log.info("================================== saved =============================================");

        userRepository.flush();
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        publishUserEvent(user.getEmail(), userDTO.getPin());
        return user;
    }

    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase().trim());
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

        log.info("================================ user to be saved====================================");
        log.info(user.toString());

        userRepository.save(user);

        log.info("================================== saved =============================================");

        userRepository.flush();
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        publishUserEvent(user.getEmail(), userDTO.getPin());
        return user;
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<AdminUserDTO> updateUser(AdminUserDTO userDTO) {
        return Optional
            .of(userRepository.findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                this.clearUserCaches(user);
                user.setLogin(userDTO.getLogin().toLowerCase());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                if (userDTO.getEmail() != null) {
                    user.setEmail(userDTO.getEmail().toLowerCase());
                }
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();

                userDTO
                    .getAuthorities()
                    .stream()
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(managedAuthorities::add);
                this.clearUserCaches(user);

                log.debug("Changed Information for User: {}", user);

                publishUserEvent(user.getEmail(), userDTO.getPin());
                return user;
            })
            .map(AdminUserDTO::new);
    }

    public void deleteUser(String login) {
        userRepository
            .findOneByLogin(login)
            .ifPresent(user -> {
                userRepository.delete(user);
                this.clearUserCaches(user);
                log.debug("Deleted User: {}", user);
            });
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     * @param imageUrl  image URL of user.
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                if (email != null) {
                    user.setEmail(email.toLowerCase());
                }
                user.setLangKey(langKey);
                user.setImageUrl(imageUrl);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
            });
    }

    @Transactional
    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                String currentEncryptedPassword = user.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new InvalidPasswordException();
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedPassword);
                this.clearUserCaches(user);
                log.debug("Changed password for User: {}", user);
            });
    }

    @Transactional
    public void changePasswordAdmin(String login, String newPassword) {
        if (login.equals("admin")) {
            throw new BadRequestAlertException("Admin password cannot be changed", "userManagement", "");
        }

        Optional<String> currentLoginOptional = SecurityUtils.getCurrentUserLogin();
        if (currentLoginOptional.isPresent()) {
            if (currentLoginOptional.get().equals(login)) {
                throw new BadRequestAlertException("Self password cannot be changed", "userManagement", "");
            }
        }

        userRepository
            .findOneByLogin(login)
            .ifPresent(user -> {
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedPassword);
                this.clearUserCaches(user);
                log.debug("Password changed by admin for User: {}", user);
            });
    }

    @Transactional(readOnly = true)
    public Page<AdminUserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(AdminUserDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllPublicUsers(Pageable pageable) {
        return userRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        final String principal = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new SecurityException("User not Logged-In"));
        return Optional.ofNullable(
            userRepository
                .findOneWithAuthoritiesByLogin(principal)
                .orElseGet(() -> userRepository.findOneWithAuthoritiesByEmailIgnoreCase(principal).orElse(null))
        );
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(user -> {
                log.debug("Deleting not activated user {}", user.getLogin());
                userRepository.delete(user);
                this.clearUserCaches(user);
            });
    }

    /**
     * Gets a list of all the authorities.
     *
     * @return a list of all the authorities.
     */
    @Transactional(readOnly = true)
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }

    public Set<User> getUserForAuthority(String authority) {
        List<String> roleList = authorityMapperService.getRoleForAuthority(authority);
        Set<User> userList = new HashSet<>();
        for (String role : roleList) {
            userRepository
                .findAll()
                .forEach(user -> {
                    List<String> authorities = user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toList());
                    if (authorities.contains(role)) {
                        userList.add(user);
                    }
                });
        }
        return userList;
    }

    public User createUserIfNotExists(final CustomLdapUserDetails principal) {
        return userRepository
            .findOneByLogin(principal.getUsername().trim().toLowerCase())
            .orElseGet(() ->
                userRepository
                    .findByEmail(principal.getEmail().trim().toLowerCase())
                    .orElseGet(() ->
                        createUser(
                            new UserDTO(
                                principal.getUsername(),
                                principal.getDisplayName(),
                                principal.getEmail(),
                                "http://10.42.54.19:8189/da/docs/x650100/Emp_img/" + principal.getPin() + ".jpg",
                                true,
                                AuthoritiesConstants.EMPLOYEE,
                                AuthoritiesConstants.USER
                            )
                        )
                    )
            );
    }

    public void publishUserEvent(String email, String pin) {
        if (email != null && pin != null) {
            eventPublisher.publishEvent(new EmployeeRegistrationEvent(this, email, pin));
        }
    }

    public Page<UserDTO> UserSearch(UserFilterDto userFilterDto, Pageable pageable) {
        if (userFilterDto.getAuthorities() != "") {
            return userRepository
                .findAllByEmailOrLoginIgnoreCaseContainingAndAuthoritiesName(
                    userFilterDto.getSearchText(),
                    userFilterDto.getSearchText(),
                    userFilterDto.getAuthorities(),
                    pageable
                )
                .map(UserDTO::new);
        } else {
            return userRepository
                .findAllByEmailOrLoginIgnoreCaseContaining(userFilterDto.getSearchText(), userFilterDto.getSearchText(), pageable)
                .map(UserDTO::new);
        }
    }
}
