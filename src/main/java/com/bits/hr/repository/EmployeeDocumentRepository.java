package com.bits.hr.repository;

import com.bits.hr.domain.EmployeeDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface EmployeeDocumentRepository extends JpaRepository<EmployeeDocument, Long> {

    @Query(value = "FROM EmployeeDocument e WHERE e.pin LIKE LOWER(CONCAT('%',:pin,'%')) ORDER BY e.fileName ASC")
    Page<EmployeeDocument> getAllByPinOrderByFileName(@Param("pin") String pin, Pageable pageable);

    boolean existsByFileNameAndPin(String fileName, String pin);
}
