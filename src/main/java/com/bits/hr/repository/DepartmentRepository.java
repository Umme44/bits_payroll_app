package com.bits.hr.repository;

import com.bits.hr.domain.Department;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Department entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findDepartmentByDepartmentNameIgnoreCase(String departmentName);

    @Query("select department from Department department order by department.departmentName asc ")
    List<Department> findAllOrderByName();
}
