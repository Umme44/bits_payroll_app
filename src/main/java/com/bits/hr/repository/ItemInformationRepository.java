package com.bits.hr.repository;

import com.bits.hr.domain.Department;
import com.bits.hr.domain.ItemInformation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ItemInformation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemInformationRepository extends JpaRepository<ItemInformation, Long> {
    @Query("select itemInformation from ItemInformation itemInformation where itemInformation.createdBy.login = ?#{principal.username}")
    List<ItemInformation> findByCreatedByIsCurrentUser();

    @Query("select itemInformation from ItemInformation itemInformation where itemInformation.updatedBy.login = ?#{principal.username}")
    List<ItemInformation> findByUpdatedByIsCurrentUser();

    Optional<ItemInformation> findByCode(String code);

    @Query("select model from ItemInformation model where model.department.id = :departmentId")
    List<ItemInformation> findByDepartmentId(long departmentId);

    @Query("select count(model) from ItemInformation model where model.department.id = :departmentId")
    long countByDepartmentId(long departmentId);

    @Query("select distinct model.department from ItemInformation model")
    List<Department> findAllDepartments();

    @Query("select model from ItemInformation model " + "where :departmentId is null or model.department.id = :departmentId")
    Page<ItemInformation> findAll(Long departmentId, Pageable pageable);
}
