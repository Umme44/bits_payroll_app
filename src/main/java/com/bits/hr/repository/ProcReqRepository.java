package com.bits.hr.repository;

import com.bits.hr.domain.ProcReq;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ProcReq entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcReqRepository extends JpaRepository<ProcReq, Long> {
    @Query("select model from ProcReq model where model.procReqMaster.id = :procReqMasterId")
    List<ProcReq> findByProcReqMasterId(long procReqMasterId);
}
