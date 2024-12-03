package com.bits.hr.repository;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeStaticFile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the EmployeeStaticFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeStaticFileRepository extends JpaRepository<EmployeeStaticFile, Long> {
    @Query("select es from EmployeeStaticFile es where es.employee.pin=:pin")
    Optional<EmployeeStaticFile> findEmployeeStaticFileByPin(String pin);

    @Query(
        "select es from EmployeeStaticFile es " +
        "where lower(es.employee.fullName) like concat('%', :searchText, '%') " +
        "or es.employee.pin like concat('%', :searchText, '%') "
    )
    Page<EmployeeStaticFile> findAll(Pageable pageable, String searchText);

    @Query(value = "select es.employee from EmployeeStaticFile es")
    List<Employee> findAllEmployee();
}
