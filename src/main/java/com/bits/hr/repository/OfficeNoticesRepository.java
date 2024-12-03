package com.bits.hr.repository;

import com.bits.hr.domain.OfficeNotices;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the OfficeNotices entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OfficeNoticesRepository extends JpaRepository<OfficeNotices, Long> {
    @Query(
        value = "select model from OfficeNotices model where " +
        " ( model.publishTo is null or model.publishTo >= CURRENT_DATE ) " +
        "AND ( model.publishForm is null or model.publishForm <= CURRENT_DATE )" +
        "AND model.status='PUBLISHED' "
    )
    List<OfficeNotices> findEffectiveNoticeForToday();

    @Query(value = "select model from OfficeNotices model where model.status = 'PUBLISHED' ")
    Page<OfficeNotices> findRecentOfficeNotices(Pageable pageable);

    @Query(value = "select model from OfficeNotices model where model.status = 'PUBLISHED' order by model.createdAt desc ")
    Page<OfficeNotices> findAllNotices(Pageable pageable);
}
