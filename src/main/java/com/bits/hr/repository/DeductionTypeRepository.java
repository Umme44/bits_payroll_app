package com.bits.hr.repository;

import com.bits.hr.domain.DeductionType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the DeductionType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeductionTypeRepository extends JpaRepository<DeductionType, Long> {
    @Query(value = " select model from DeductionType model where lower(trim(model.name))=lower(trim(:name)) ")
    Optional<DeductionType> findDeductionTypeByName(String name);
}
