package com.bits.hr.repository;

import com.bits.hr.domain.ProRataFestivalBonus;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ProRataFestivalBonus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProRataFestivalBonusRepository extends JpaRepository<ProRataFestivalBonus, Long> {
    @Query(
        value = "select model from ProRataFestivalBonus  model " +
        "where model.date >=:startDate and model.date <=:endDate and model.employee.id=:employeeId"
    )
    List<ProRataFestivalBonus> findAllBetween(Long employeeId, LocalDate startDate, LocalDate endDate);

    @Query(
        "select model from ProRataFestivalBonus model " +
        "where " +
        "(" +
        " lower(model.employee.fullName) like lower(concat('%', :searchText,'%')) OR " +
        "lower(model.employee.pin) like lower(concat('%', :searchText,'%')) " +
        ") AND " +
        " (( cast(:startDate as date) is null OR cast(:endDate as date) is null ) OR (model.date between :startDate AND :endDate)) " +
        "ORDER BY model.employee.fullName asc "
    )
    Page<ProRataFestivalBonus> findAll(String searchText, LocalDate startDate, LocalDate endDate, Pageable pageable);
}
