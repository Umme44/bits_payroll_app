package com.bits.hr.repository;

import com.bits.hr.domain.PfAccount;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PfAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PfAccountRepository extends JpaRepository<PfAccount, Long> {
    @Query(value = " select model from PfAccount model where model.pin=:pin and model.pfCode=:pfCode")
    Optional<PfAccount> getPfAccountByPinAndPfCode(String pin, String pfCode);

    @Query(value = " select model from PfAccount model where model.pin = :pin")
    List<PfAccount> getPfAccountsByPin(String pin);

    @Query(value = "select model from PfAccount model " + "where :pin is null or model.pin = :pin " + "order by model.pin")
    Page<PfAccount> findAllByPin(String pin, Pageable pageable);

    @Query(value = " select model from PfAccount model where model.pin = :pin")
    Optional<PfAccount> getPfAccountByPin(String pin);

    @Query(value = " select model from PfAccount model where model.pfCode = :pfCode")
    Optional<PfAccount> getPfAccountByPfCode(String pfCode);
}
