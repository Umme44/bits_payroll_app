package com.bits.hr.repository;

import com.bits.hr.domain.Band;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Band entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BandRepository extends JpaRepository<Band, Long> {
    Optional<Band> findBandByBandName(String bandName);

    @Query(value = "select model from Band model where model.bandName = :bandName")
    Optional<Band> findByBandName(String bandName);

    @Query("select band from Band band where band.createdBy.login = ?#{principal.username}")
    List<Band> findByCreatedByIsCurrentUser();

    @Query("select band from Band band where band.updatedBy.login = ?#{principal.username}")
    List<Band> findByUpdatedByIsCurrentUser();

    @Query("select band from Band band order by length(band.bandName), band.bandName asc")
    List<Band> findAllOrderByName();

    @Query("select band from Band band order by length(band.bandName), band.bandName asc")
    Page<Band> findAll(Pageable pageable);
}
