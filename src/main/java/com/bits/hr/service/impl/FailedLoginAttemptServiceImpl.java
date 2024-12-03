package com.bits.hr.service.impl;

import com.bits.hr.domain.FailedLoginAttempt;
import com.bits.hr.repository.FailedLoginAttemptRepository;
import com.bits.hr.service.FailedLoginAttemptService;
import io.undertow.util.BadRequestException;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

/**
 * Service Implementation for managing {@link FailedLoginAttempt}.
 */
@Service
@Transactional
public class FailedLoginAttemptServiceImpl implements FailedLoginAttemptService {

    private final Logger log = LoggerFactory.getLogger(FailedLoginAttemptServiceImpl.class);

    private final FailedLoginAttemptRepository failedLoginAttemptRepository;

    public FailedLoginAttemptServiceImpl(FailedLoginAttemptRepository failedLoginAttemptRepository) {
        this.failedLoginAttemptRepository = failedLoginAttemptRepository;
    }

    /*
     * if already failed attempt > 3 && lastTimeFailedAttempt < 25 min
     * -> BadRequestException("You have exceeded the maximum number of failed login attempts. Please try again after 25 minutes.")
     * */
    @Override
    public void validateFailedAttemptIfAny(String userName) {
        failedLoginAttemptRepository
            .findByUsername(userName)
            .ifPresent(failedLoginAttempt -> {
                if (
                    failedLoginAttempt.getContinuousFailedAttempts() > 3 &&
                        Duration.between(failedLoginAttempt.getLastFailedAttempt(), Instant.now()).toMinutes() < 25
                ) {
                    throw new BadCredentialsException(
                        "You have exceeded the maximum number of failed login attempts. Please try again after 25 minutes."
                    );
                }
            });
    }

    /*
     * if user id and password correct:
     * reset failed attempt
     * */
    @Override
    public void resetFailedAttemptIfPresent(String userName) {
        failedLoginAttemptRepository
            .findByUsername(userName)
            .ifPresent(failedLoginAttempt -> {
                failedLoginAttempt.setContinuousFailedAttempts(0);
                failedLoginAttempt.setLastFailedAttempt(Instant.now());
            });
    }

    /*
     * user id - pass wrong
     * -> if user id valid
     *   -> ++failed attempt
     * */
    @Override
    public void recordIncidentForBadCredentials(String userName) {
        Optional<FailedLoginAttempt> failedLoginAttempt = failedLoginAttemptRepository.findByUsername(userName);
        if (failedLoginAttempt.isPresent()) {
            failedLoginAttempt.get().setContinuousFailedAttempts(failedLoginAttempt.get().getContinuousFailedAttempts() + 1);
            failedLoginAttempt.get().setLastFailedAttempt(Instant.now());
            failedLoginAttemptRepository.save(failedLoginAttempt.get());
        } else {
            FailedLoginAttempt newFailedLoginAttempt = new FailedLoginAttempt();
            newFailedLoginAttempt.setUserName(userName);
            newFailedLoginAttempt.setContinuousFailedAttempts(1);
            newFailedLoginAttempt.setLastFailedAttempt(Instant.now());
            failedLoginAttemptRepository.save(newFailedLoginAttempt);
        }
    }

//    public void findFailedLoginAttemptWithMultipleFailuresNew(String username) {
//        Optional<FailedLoginAttempt> failedLoginAttemptOpt = failedLoginAttemptRepository.findFailedLoginAttemptWithMultipleFailures(username);
//                if(failedLoginAttemptOpt.isPresent()){
//                    failedLoginAttemptOpt.get().setContinuousFailedAttempts(0);
//                    failedLoginAttemptOpt.get().setLastFailedAttempt(Instant.now());
//                }
//        };
//}

//
//    public Integer findContinuousFailedAttemptsZero(String username) {
//        Optional<FailedLoginAttempt> failedLoginAttemptOpt = failedLoginAttemptRepository.findContinuousFailedAttemptsZero(username);
//
//        if (failedLoginAttemptOpt.isPresent()) {
//            FailedLoginAttempt attempt = failedLoginAttemptOpt.get();
//            System.out.println(attempt);
//            return attempt.getContinuousFailedAttempts();
//        } else {
//            throw new EntityNotFoundException("No failed login attempt record found for user: " + username);
//        }
//    }


    //New API//
//    public String findFailedLoginAttemptByUsername(String username) {
//        Optional<FailedLoginAttempt> failedLoginAttemptOpt = failedLoginAttemptRepository.findFailedLoginAttemptByUsernameNew(username);
//        if (failedLoginAttemptOpt.isPresent()) {
//            FailedLoginAttempt attempt = failedLoginAttemptOpt.get();
//            return attempt.getUserName();
//        }
//        else{
//            throw new EntityNotFoundException("No failed login attempt record found for user: " + username);
//        }
//    }

    public Optional<FailedLoginAttempt> getFailedLoginAttemptUserName(String username) {
        Optional<FailedLoginAttempt> failedLoginAttemptOpt = failedLoginAttemptRepository.findFailedLoginAttemptByUsernameNew(username);

        if (failedLoginAttemptOpt.isPresent()) {
            return failedLoginAttemptOpt; //
        } else {
            throw new EntityNotFoundException("No failed login attempt record found for user: " + username);
        }
    }


//    public void findFailedLoginAttemptWithMultipleFailures(String username) {
//        Optional<FailedLoginAttempt> failedLoginAttemptOpt = failedLoginAttemptRepository.findFailedLoginAttemptWithMultipleFailures(username);
//
//        if (failedLoginAttemptOpt.isPresent()) {
//            FailedLoginAttempt attempt = failedLoginAttemptOpt.get();
//            attempt.setContinuousFailedAttempts(0);
//            failedLoginAttemptRepository.save(attempt);
//        }
//        else {
//            throw new EntityNotFoundException("No login attempt record found for user: " + username);
//        }
//    }
    public void findFailedLoginAttemptWithMultipleFailuresNew(String username) {
        Optional<FailedLoginAttempt> failedLoginAttemptOpt = failedLoginAttemptRepository.findFailedLoginAttemptWithMultipleFailures(username);

        if (failedLoginAttemptOpt.isPresent()) {
            FailedLoginAttempt attempt = failedLoginAttemptOpt.get();
            attempt.setContinuousFailedAttempts(0);
            failedLoginAttemptRepository.save(attempt);
        } else {
            throw new EntityNotFoundException("No login attempt record found for user: " + username);
        }
    }



}




