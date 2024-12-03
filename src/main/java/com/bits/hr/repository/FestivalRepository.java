package com.bits.hr.repository;

import com.bits.hr.domain.Festival;
import com.bits.hr.domain.enumeration.Religion;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Festival entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FestivalRepository extends JpaRepository<Festival, Long> {
    @Query(value = " select model from Festival model order by model.bonusDisbursementDate ")
    List<Festival> findAll();

    @Query(
        value = " select model from Festival model " +
        " where lower(model.title) like :searchText " +
        " order by model.bonusDisbursementDate desc "
    )
    Page<Festival> findAll(String searchText, Pageable pageable);

    @Query(
        value = " select model from Festival model " +
        " where model.isProRata<>true " +
        " and (model.religion='ALL' or model.religion = :religion) " +
        " and (model.festivalDate between :startDate and :endDate )"
    )
    List<Festival> getFestivalsBetweenDatesAndApplicableReligionExcludeProRata(LocalDate startDate, LocalDate endDate, Religion religion);

    @Query(
        value = " select model from Festival model " +
        "where model.isProRata<>true " +
        " and model.festivalDate between :startDate and :endDate " +
        "order by model.festivalDate"
    )
    List<Festival> getFestivalsBetweenFestivalDateExcludeProRata(LocalDate startDate, LocalDate endDate);

    @Query(
        value = " select model from Festival model " +
        "where model.isProRata<>true and (model.religion='ALL' or model.religion = :religion) " +
        "and ( model.bonusDisbursementDate >=:startDate and model.bonusDisbursementDate <=:endDate) "
    )
    List<Festival> getFestivalsBetweenDisbursementDate(LocalDate startDate, LocalDate endDate, Religion religion);

    @Query(
        value = " select model from Festival model " +
        "where model.isProRata=true " +
        "and ( model.bonusDisbursementDate >=:startDate and model.bonusDisbursementDate <=:endDate) "
    )
    List<Festival> getProRataFestivalsBetweenDisbursementDate(LocalDate startDate, LocalDate endDate);

    @Query("select model from Festival model where model.bonusDisbursementDate >= :bonusDisbursementDate")
    List<Festival> getAllByBonusDisbursementDateAfterAndEqual(LocalDate bonusDisbursementDate);

    @Query(value = "select model from Festival model where model.title=:festivalTitle")
    Optional<Festival> findDuplicateFestivalByTitleName(String festivalTitle);
}
