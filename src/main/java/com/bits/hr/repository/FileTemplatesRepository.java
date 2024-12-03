package com.bits.hr.repository;

import com.bits.hr.domain.FileTemplates;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the FileTemplates entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FileTemplatesRepository extends JpaRepository<FileTemplates, Long> {
    @Query(value = "select model from FileTemplates model where model.type='POLICIES' and model.isActive=true")
    Page<FileTemplates> findAllPolicies(Pageable pageable);

    @Query(value = "select model from FileTemplates model where model.type='TEMPLATES' and model.isActive=true ")
    Page<FileTemplates> findAllTemplates(Pageable pageable);

    @Query(value = "select model from FileTemplates model where model.type='FORMS' and model.isActive=true")
    Page<FileTemplates> findAllForms(Pageable pageable);

    @Query(
        value = "select model from FileTemplates model where model.type='POLICIES' and model.accessPrivilege='GENERAL' and model.isActive=true "
    )
    Page<FileTemplates> findAllPoliciesCommon(Pageable pageable);

    @Query(
        value = "select model from FileTemplates model where model.type='TEMPLATES' and model.accessPrivilege='GENERAL' and model.isActive=true "
    )
    Page<FileTemplates> findAllTemplatesCommon(Pageable pageable);

    @Query(
        value = "select model from FileTemplates model where model.type='FORMS' and model.accessPrivilege='GENERAL' and model.isActive=true "
    )
    Page<FileTemplates> findAllFormsCommon(Pageable pageable);

    @Query(
        value = "select model from FileTemplates model " +
        "where (lower(model.title) like lower(concat('%',:searchText,'%'))) " +
        "AND (:fileType='' OR model.type=:fileType)"
    )
    Page<FileTemplates> findAllByFileType(String searchText, String fileType, Pageable pageable);

    @Query(
        value = "select model from FileTemplates model " +
        "where (lower(model.title) like lower(concat('%',:searchText,'%'))) " +
        "AND (:fileType='' OR model.type=:fileType) " +
        "AND model.accessPrivilege='GENERAL' " +
        "AND model.isActive=true"
    )
    Page<FileTemplates> findAllByFileTypeCommon(String searchText, String fileType, Pageable pageable);

    @Query(value = "select model from FileTemplates model " + "     where lower(model.title) like lower(concat('%', :srcTxt,'%'))")
    Page<FileTemplates> findAll(Pageable pageable, String srcTxt);

    @Query(value = "select model.title from FileTemplates model")
    List<String> findAllTitlesAdmin();

    @Query(value = "select model.title from FileTemplates model where model.accessPrivilege = 'GENERAL' and model.isActive= true")
    List<String> findAllTitlesCommon();

    @Query(
        value = "select model from FileTemplates model where " +
        " model.id = :id and model.accessPrivilege='GENERAL' and model.isActive=true"
    )
    Optional<FileTemplates> findByIdUser(Long id);
}
