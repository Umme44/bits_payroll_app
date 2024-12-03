package com.bits.hr.repository;

import com.bits.hr.domain.Offer;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Offer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    @Query(value = "select model from Offer model order by model.createdAt desc")
    List<Offer> getRecentOffer(Pageable pageable);
}
