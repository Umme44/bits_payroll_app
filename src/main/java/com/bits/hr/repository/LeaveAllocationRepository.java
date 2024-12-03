package com.bits.hr.repository;

import com.bits.hr.domain.LeaveAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the LeaveAllocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeaveAllocationRepository extends JpaRepository<LeaveAllocation, Long> {}
