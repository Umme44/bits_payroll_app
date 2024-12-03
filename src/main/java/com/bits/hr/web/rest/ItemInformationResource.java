package com.bits.hr.web.rest;

import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.ItemInformationService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.ItemInformationDTO;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.ItemInformation}.
 */
@RestController
@RequestMapping("/api/procurement-mgt")
public class ItemInformationResource {

    private final Logger log = LoggerFactory.getLogger(ItemInformationResource.class);

    private static final String ENTITY_NAME = "itemInformation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemInformationService itemInformationService;

    private final CurrentEmployeeService currentEmployeeService;

    public ItemInformationResource(ItemInformationService itemInformationService, CurrentEmployeeService currentEmployeeService) {
        this.itemInformationService = itemInformationService;
        this.currentEmployeeService = currentEmployeeService;
    }

    /**
     * {@code POST  /item-information} : Create a new itemInformation.
     *
     * @param itemInformationDTO the itemInformationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemInformationDTO, or with status {@code 400 (Bad Request)} if the itemInformation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/item-information")
    public ResponseEntity<ItemInformationDTO> createItemInformation(@Valid @RequestBody ItemInformationDTO itemInformationDTO)
        throws URISyntaxException {
        log.debug("REST request to save ItemInformation : {}", itemInformationDTO);
        if (itemInformationDTO.getId() != null) {
            throw new BadRequestAlertException("A new itemInformation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        itemInformationDTO.setCreatedById(currentEmployeeService.getCurrentUserId().get());
        ItemInformationDTO result = itemInformationService.create(itemInformationDTO);
        return ResponseEntity
            .created(new URI("/api/item-information/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /item-information} : Updates an existing itemInformation.
     *
     * @param itemInformationDTO the itemInformationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemInformationDTO,
     * or with status {@code 400 (Bad Request)} if the itemInformationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemInformationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/item-information")
    public ResponseEntity<ItemInformationDTO> updateItemInformation(@Valid @RequestBody ItemInformationDTO itemInformationDTO)
        throws URISyntaxException {
        log.debug("REST request to update ItemInformation : {}", itemInformationDTO);
        if (itemInformationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        itemInformationDTO.setUpdatedById(currentEmployeeService.getCurrentUserId().get());

        ItemInformationDTO result = itemInformationService.save(itemInformationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemInformationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /item-information} : get all the itemInformations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemInformations in body.
     */
    @GetMapping("/item-information")
    public ResponseEntity<List<ItemInformationDTO>> getAllItemInformation(
        @RequestParam(required = false) Long departmentId,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of ItemInformation");
        Page<ItemInformationDTO> page = itemInformationService.findAll(departmentId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /item-information/:id} : get the "id" itemInformation.
     *
     * @param id the id of the itemInformationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemInformationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/item-information/{id}")
    public ResponseEntity<ItemInformationDTO> getItemInformation(@PathVariable Long id) {
        log.debug("REST request to get ItemInformation : {}", id);
        Optional<ItemInformationDTO> itemInformationDTO = itemInformationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemInformationDTO);
    }

    /**
     * {@code DELETE  /item-information/:id} : delete the "id" itemInformation.
     *
     * @param id the id of the itemInformationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/item-information/{id}")
    public ResponseEntity<Void> deleteItemInformation(@PathVariable Long id) {
        log.debug("REST request to delete ItemInformation : {}", id);
        itemInformationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/item-information/is-code-unique")
    public ResponseEntity<Boolean> findCodeIsUnique(@RequestParam(required = false) Long id, @RequestParam String code) {
        log.debug("REST request to get code is unique");
        boolean result = itemInformationService.isCodeUnique(id, code);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/item-information/generate-unique-item-code")
    public ResponseEntity<String> generateUniqueCode() {
        log.debug("REST request to generate unique code");
        String result = itemInformationService.generateNextItemCode();
        return ResponseEntity.ok().body(result);
    }
}
