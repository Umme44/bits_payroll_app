package com.bits.hr.repository;

import com.bits.hr.domain.EventLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the EventLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventLogRepository extends JpaRepository<EventLog, Long> {
    @Query("select eventLog from EventLog eventLog where eventLog.performedBy.login = ?#{principal.username}")
    List<EventLog> findByPerformedByIsCurrentUser();
}
