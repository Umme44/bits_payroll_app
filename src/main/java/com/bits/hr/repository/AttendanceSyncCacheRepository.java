package com.bits.hr.repository;

import com.bits.hr.domain.AttendanceSyncCache;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AttendanceSyncCache entity.
 */

@Repository
public interface AttendanceSyncCacheRepository extends JpaRepository<AttendanceSyncCache, Long> {
    Optional<AttendanceSyncCache> findTopByOrderByIdDesc();

    List<AttendanceSyncCache> findAllByIdGreaterThanOrderByTimestampAsc(Long id);

    @Query(value = "SELECT model FROM AttendanceSyncCache model WHERE model.id> 1 GROUP BY model.employeePin, day(model.timestamp) ORDER BY model.timestamp ASC ")
    List<AttendanceSyncCache> findAllByIDGreaterThanOrderByTimestampGroupByEmployeeIdAndDay(Long id);

    Long deleteByTimestampLessThan(Instant timestamp);
}
