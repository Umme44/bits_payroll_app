package com.bits.hr.repository;

import com.bits.hr.domain.PfCollection;
import com.bits.hr.domain.enumeration.PfCollectionType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PfCollection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PfCollectionRepository extends JpaRepository<PfCollection, Long> {
    @Query(
        value = " select model from PfCollection model " +
        " where model.pfAccount.pfCode=:pfCode " +
        " and model.year=:year and model.month=:month " +
        " and model.collectionType='MONTHLY'"
    )
    Optional<PfCollection> getMonthlyPfCollection(String pfCode, int year, int month);

    @Query(
        value = " select model from PfCollection model " +
        " where model.pfAccount.pfCode=:pfCode " +
        " and model.year=:year and model.month=:month " +
        " and model.collectionType=:pfCollectionType"
    )
    Optional<PfCollection> getPfCollection(String pfCode, int year, int month, PfCollectionType pfCollectionType);

    @Query(
        value = " select model from PfCollection model " +
        " where model.pfAccount.pfCode = :pfCode and model.pfAccount.pin = :pin " +
        " and model.collectionType = 'OPENING_BALANCE'"
    )
    Optional<PfCollection> getOpeningPfBalance(String pfCode, String pin);

    @Query(
        value = " select distinct model.year from PfCollection model " +
        " where model.pfAccount.id =:pfAccountId and model.collectionType = 'MONTHLY' " +
        " order by model.year"
    )
    List<Integer> getListOfYears(long pfAccountId);

    @Query(
        value = " select distinct model.year from PfCollection model " +
        " where model.pfAccount.id =:pfAccountId and model.collectionType = 'MONTHLY' " +
        "and ( 0 =:year or model.year =: year) " +
        " order by model.year"
    )
    List<Integer> getYearsFromPfCollection(long pfAccountId, int year);

    @Query(
        value = " select model from PfCollection model " +
        " where model.pfAccount.id =:pfAccountId and model.year=:year " +
        " order by model.month"
    )
    List<PfCollection> getPfCollectionByYear(long pfAccountId, int year);

    @Query(
        value = " select model from PfCollection model " +
        " where model.pfAccount.id =:pfAccountId " +
        " and model.year = :year " +
        "and " +
        "   (" +
        "       (0=:startingMonth or model.month >= :startingMonth) " +
        "       and (0=:endingMonth or model.month <=:endingMonth)" +
        "   )" +
        " and model.collectionType = 'MONTHLY'" +
        " order by model.month"
    )
    List<PfCollection> getPfCollectionBetweenYearAndMonth(long pfAccountId, int year, int startingMonth, int endingMonth);

    //    @Query(
    //        value = " select model from PfCollection model " +
    //        " where model.pfAccount.id =:pfAccountId " +
    //        " and model.year = :year " +
    //        "and " +
    //        "   (" +
    //        "       (0=:startingMonth or model.month >= :startingMonth) " +
    //        "       and (0=:endingMonth or model.month <=:endingMonth)" +
    //        "   )" +
    //        " and model.collectionType = 'MONTHLY'" +
    //        " order by model.month"
    //    )
    //    List<PfCollection> getPfCollectionBetweenYearAndMonth(long pfAccountId, int year, int startingMonth, int endingMonth);

    @Query(
        value = " select model from PfCollection model " + " where model.pfAccount.id=:pfAccountId and model.collectionType = 'MONTHLY' "
    )
    List<PfCollection> getPfMonthlyCollectionByPfAccount(long pfAccountId);

    @Query(value = " select model from PfCollection model " + " where model.pfAccount.id=:pfAccountId and model.collectionType = 'ARREAR' ")
    List<PfCollection> getArrearPfCollectionByPfAccount(long pfAccountId);

    @Query(
        value = " select sum(model.employeeContribution) from PfCollection model " +
        " where model.pfAccount.id=:pfAccountId " +
        " and model.collectionType='MONTHLY'"
    )
    Double getPfCollection(long pfAccountId);

    @Query(
        value = " select sum(model.employeeInterest) from PfCollection model " +
        " where model.pfAccount.id=:pfAccountId and model.collectionType = 'MONTHLY' "
    )
    Double getTotalEmployeeInterests(long pfAccountId);

    @Query(
        value = " select sum(model.employerInterest) from PfCollection model " +
        " where model.pfAccount.id=:pfAccountId and model.collectionType = 'MONTHLY' "
    )
    Double getTotalEmployerInterests(long pfAccountId);

    @Query(
        value = "select coalesce(sum(collection.employeeInterest), 0) " +
        " from PfCollection collection " +
        " where (collection.pfAccount.id = :pfAccountId and collection.year < :year) " +
        "    or (collection.pfAccount.id = :pfAccountId and collection.year = :year and collection.month <= :month)"
    )
    Double getTotalEmployeeInterestsTillMonthAndYear(long pfAccountId, int month, int year);

    // pf interest till year and month (company)
    @Query(
        value = " select coalesce(sum(collection.employerInterest), 0) " +
        " from PfCollection collection " +
        " where (collection.pfAccount.id = :pfAccountId and collection.year < :year) " +
        "    or (collection.pfAccount.id = :pfAccountId and collection.year = :year and collection.month <= :month) "
    )
    Double getTotalEmployerInterestsTillMonthAndYear(long pfAccountId, int month, int year);

    // consolidated monthly pf collection (member)
    @Query(
        value = "select coalesce(sum(model.employeeContribution), 0) from PfCollection model where model.pfAccount.id=:pfAccountId and model.collectionType = 'MONTHLY' " +
        "and model.year <= :year and model.month <= :month"
    )
    Double getConsolidatedOfMonthlyEmployeePfContributionByYearAndMonth(long pfAccountId, int month, int year);

    // consolidated monthly pf collection (company)
    @Query(
        value = "select coalesce(sum(model.employerContribution), 0) from PfCollection model where model.pfAccount.id=:pfAccountId and model.collectionType = 'MONTHLY' " +
        "and model.year <= :year and model.month <= :month"
    )
    Double getConsolidatedOfMonthlyEmployerPfContributionByYearAndMonth(long pfAccountId, int month, int year);

    // one individual year monthly pf collection (member)
    @Query(
        value = "select coalesce(sum(model.employeeContribution), 0) from PfCollection model where model.pfAccount.id=:pfAccountId and model.collectionType = 'MONTHLY' " +
        "and model.year = :year"
    )
    Double getOneYearMonthlyEmployeePfContribution(long pfAccountId, int year);

    // one individual year monthly pf collection (company)
    @Query(
        value = "select coalesce(sum(model.employerContribution), 0) from PfCollection model where model.pfAccount.id=:pfAccountId and model.collectionType = 'MONTHLY' " +
        "and model.year = :year"
    )
    Double getOneYearMonthlyEmployerPfContribution(long pfAccountId, int year);

    // one individual year monthly pf collection till month (member)
    @Query(
        value = "select coalesce(sum(model.employeeContribution), 0) from PfCollection model where model.pfAccount.id=:pfAccountId and model.collectionType = 'MONTHLY' " +
        "and model.year = :year and model.month <= :month"
    )
    Double getOneYearMonthlyEmployeePfContributionTillMonth(long pfAccountId, int month, int year);

    // one individual year monthly pf collection till month (company)
    @Query(
        value = "select coalesce(sum(model.employeeContribution), 0) from PfCollection model where model.pfAccount.id=:pfAccountId and model.collectionType = 'MONTHLY' " +
        "and model.year = :year and model.month <= :month"
    )
    Double getOneYearMonthlyEmployerPfContributionTillMonth(long pfAccountId, int month, int year);

    @Query(
        value = "select model from PfCollection model " +
        "where model.collectionType = 'MONTHLY' order by model.pfAccount.pin,model.year,model.month"
    )
    List<PfCollection> getMonthlyPfCollection();

    @Query(
        value = "select model from PfCollection model " +
        "where model.collectionType = 'OPENING_BALANCE' order by model.pfAccount.pin,model.year,model.month"
    )
    List<PfCollection> getOpeningBalancePfCollection();

    @Query(value = "select distinct model.year from PfCollection model " + "where model.year is not null " + "order by model.year desc ")
    List<Integer> getListOfYearsForOverallPfAmountReport();

    @Query(value = "select model from PfCollection model order by model.pfAccount.pin, model.year, model.month")
    List<PfCollection> getAllPfCollectionSortByPinYearAndMonth();

    @Query(
        value = "select model from PfCollection model " +
        "where model.pfAccount.pin =:pin " +
        "and ( " +
        "       model.collectionType = 'OPENING_BALANCE' " +
        "       or " +
        "       ( model.collectionType = 'MONTHLY' and model.year <= :year and model.month <= :month ) " +
        "    ) " +
        "order by model.pfAccount.pin"
    )
    List<PfCollection> getAllPfCollectionTillYearByPin(String pin, int year, int month);

    @Query(value = "select model from PfCollection model where model.collectionType <> 'OPENING_BALANCE' order by model.year, model.month")
    List<PfCollection> getVeryFirstPfCollections(Pageable pageable);

    @Query(
        value = "select model from PfCollection model where model.collectionType <> 'OPENING_BALANCE'  order by model.year desc, model.month desc "
    )
    List<PfCollection> getTheLastPfCollections(Pageable pageable);

    @Query(value = "select model from PfCollection model where model.pfAccount.id =:pfAccountId")
    List<PfCollection> getAllPfCollectionsByPfAccountId(long pfAccountId);

    @Query(
        value = "select model " +
        "from PfCollection model " +
        "where (:pfAccountId is null or model.pfAccount.id = :pfAccountId) " +
        "  and (:year is null or model.year = :year) " +
        "  and (:month is null or model.month = :month) " +
        "  and (:collectionType is null or model.collectionType = :collectionType)" +
        "  order by model.pfAccount.pin, model.year, model.month, model.collectionType"
    )
    Page<PfCollection> findAll(Pageable pageable, Long pfAccountId, Integer year, Integer month, PfCollectionType collectionType);

    @Query(value = "select model from PfCollection model order by model.pfAccount.pin, model.year, model.month")
    Page<PfCollection> findAllSortedByPinYearAndMonth(Pageable pageable);

    @Query(
        value = "select model from PfCollection model " +
        "where model.pfAccount.id = :pfAccountId " +
        "and model.year = :year and model.month = :month"
    )
    Optional<PfCollection> getPfCollectionByMonthAndYear(long pfAccountId, int year, int month);

    @Query(
        "SELECT model FROM PfCollection model WHERE model.collectionType = 'MONTHLY' " +
        "AND (( model.year = :startYear AND model.month >= :startMonth ) OR model.year > :startYear) " +
        "AND ((model.year = :endYear AND model.month <= :endMonth) OR model.year < :endYear)" +
        "ORDER BY model.pfAccount.pfCode ASC, model.year ASC, model.month ASC"
    )
    List<PfCollection> getPfCollectionBetweenDateRange(int startYear, int endYear, int startMonth, int endMonth);
}
