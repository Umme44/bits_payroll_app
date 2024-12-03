package com.bits.hr.repository;

import com.bits.hr.domain.IncomeTaxChallan;
import com.bits.hr.domain.enumeration.Month;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the IncomeTaxChallan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IncomeTaxChallanRepository extends JpaRepository<IncomeTaxChallan, Long> {
    @Query("select incomeTaxChallan.challanNo from IncomeTaxChallan incomeTaxChallan where incomeTaxChallan.aitConfig.id=:id")
    List<String> getIncomeTaxChallanNoByAitConfig(long id);

    @Query("select model from IncomeTaxChallan model where model.aitConfig.id=:aitConfigId")
    List<IncomeTaxChallan> getIncomeTaxChallanListByAitConfig(long aitConfigId);

    @Query(
        "select case when count(model) > 0 then true else false end from IncomeTaxChallan model where concat('%',lower(model.challanNo) ,'%') like concat('%',lower(:challanNo) ,'%')"
    )
    Boolean checkChallanNoIsExists(String challanNo);

    @Query(
        " select model from IncomeTaxChallan model" +
        " where model.challanNo=:challanNo " +
        " and model.year=:year " +
        " and model.month=:month "
    )
    List<IncomeTaxChallan> getIncomeTaxChallanByChallanNoAndYearAndMonth(String challanNo, int year, Month month);

    @Query(
        value = "select model " + "from IncomeTaxChallan model " + "where :aitConfigId is null " + "   or model.aitConfig.id = :aitConfigId"
    )
    Page<IncomeTaxChallan> findAll(Pageable pageable, Long aitConfigId);
}
