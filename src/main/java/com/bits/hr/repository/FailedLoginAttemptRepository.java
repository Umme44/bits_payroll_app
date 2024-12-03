package com.bits.hr.repository;

import com.bits.hr.domain.FailedLoginAttempt;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FailedLoginAttempt entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FailedLoginAttemptRepository extends JpaRepository<FailedLoginAttempt, Long> {
    @Query("select failedLoginAttempt from FailedLoginAttempt failedLoginAttempt where failedLoginAttempt.userName = :username")
    Optional<FailedLoginAttempt> findByUsername(String username);


//    @Query("select f from FailedLoginAttempt f where f.userName = :username and f.continuousFailedAttempts = 0")
//    Optional<FailedLoginAttempt> findContinuousFailedAttemptsZero(@Param("username") String username);

//    @Query("select f from FailedLoginAttempt f where f.userName = :username and f.continuousFailedAttempts = 0")
//    Optional<FailedLoginAttempt> findFailedLoginAttemptByUsername(@Param("username") String username);

    @Query("select f from FailedLoginAttempt f where f.userName = :username and f.continuousFailedAttempts >= 4")
    Optional<FailedLoginAttempt> findFailedLoginAttemptWithMultipleFailures(@Param("username") String username);



    @Query("SELECT f FROM FailedLoginAttempt f WHERE f.userName = :username AND f.continuousFailedAttempts = 0")
    Optional<FailedLoginAttempt> findFailedLoginAttemptByUsernameNew(@Param("username") String username);




}
