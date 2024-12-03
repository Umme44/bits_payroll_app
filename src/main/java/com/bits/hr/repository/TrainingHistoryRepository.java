package com.bits.hr.repository;

import com.bits.hr.domain.TrainingHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TrainingHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrainingHistoryRepository extends JpaRepository<TrainingHistory, Long> {
    @Query(value = "select model from TrainingHistory model where model.employee.id = :employeeId")
    List<TrainingHistory> findAllByEmployeeId(long employeeId);
}
