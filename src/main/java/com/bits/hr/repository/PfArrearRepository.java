package com.bits.hr.repository;

import com.bits.hr.domain.PfArrear;
import com.bits.hr.domain.enumeration.Month;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PfArrear entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PfArrearRepository extends JpaRepository<PfArrear, Long> {
    @Query("select model from PfArrear model where model.employee.id=:employeeId AND model.year =:year AND model.month =:month")
    List<PfArrear> findByEmployeeIdAndYearAndMonth(Long employeeId, int year, Month month);

    @Query(
        value = "select model from PfArrear model " +
        "where model.employee.pin like :searchText " +
        "OR lower(model.employee.fullName) like :searchText"
    )
    Page<PfArrear> findAll(Pageable pageable, String searchText);
}
