package com.bits.hr.service;

import com.bits.hr.domain.FailedLoginAttempt;
import io.undertow.util.BadRequestException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link FailedLoginAttempt}.
 */
@Service
public interface FailedLoginAttemptService {
    /*
     * if already failed attempt > 3 && lastTimeFailedAttempt < 25 min
     * -> BadRequestException("You have exceeded the maximum number of failed login attempts. Please try again after 25 minutes.")
     * */
    public void validateFailedAttemptIfAny(String userName);

    /*
     * if user id and password correct:
     * reset failed attempt
     * */
    public void resetFailedAttemptIfPresent(String userName);

    /*
     * user id - pass wrong
     * -> if user id valid
     *   -> ++failed attempt
     * */
    public void recordIncidentForBadCredentials(String userName);








}
