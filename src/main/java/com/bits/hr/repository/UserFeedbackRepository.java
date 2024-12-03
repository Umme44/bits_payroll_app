package com.bits.hr.repository;

import com.bits.hr.domain.UserFeedback;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the UserFeedback entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserFeedbackRepository extends JpaRepository<UserFeedback, Long> {
    @Query("select userFeedback from UserFeedback userFeedback where userFeedback.user.login = ?#{principal.username}")
    List<UserFeedback> findByUserIsCurrentUser();

    @Query("select userFeedback from UserFeedback userFeedback where userFeedback.user.id = :userId")
    Optional<UserFeedback> findUserFeedbackByUserId(long userId);
}
