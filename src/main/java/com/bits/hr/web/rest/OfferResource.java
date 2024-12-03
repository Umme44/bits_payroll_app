package com.bits.hr.web.rest;

import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.OfferService;
import com.bits.hr.service.dto.OfferDTO;
import com.bits.hr.service.fileOperations.fileService.OfferFileService;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.Offer}.
 */
@RestController
@RequestMapping("/api/employee-mgt")
public class OfferResource {

    private final Logger log = LoggerFactory.getLogger(OfferResource.class);

    private static final String ENTITY_NAME = "offer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OfferService offerService;
    private final OfferFileService offerFileService;

    public OfferResource(OfferService offerService, OfferFileService offerFileService) {
        this.offerService = offerService;
        this.offerFileService = offerFileService;
    }

    /**
     * {@code POST  /offers} : Create a new offer.
     *
     * @param offerDTO the offerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new offerDTO, or with status {@code 400 (Bad Request)} if the offer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/offers")
    public ResponseEntity<OfferDTO> createOffer(@Valid @RequestBody OfferDTO offerDTO) throws URISyntaxException {
        log.debug("REST request to save Offer : {}", offerDTO);
        if (offerDTO.getId() != null) {
            throw new BadRequestAlertException("A new offer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OfferDTO result = offerService.save(offerDTO);
        return ResponseEntity
            .created(new URI("/api/offers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/offers/multipart")
    public ResponseEntity<OfferDTO> createOfferMultipart(
        @RequestPart(name = "file") MultipartFile file,
        @RequestPart(name = "offer") OfferDTO offerDTO
    ) throws URISyntaxException {
        log.debug("REST request to save Offer : {}", offerDTO);

        File savedFile = offerFileService.save(file);
        offerDTO.setImagePath(savedFile.getAbsolutePath());

        if (offerDTO.getCreatedAt() == null) {
            offerDTO.setCreatedAt(LocalDate.now());
        }

        if (offerDTO.getId() != null) {
            throw new BadRequestAlertException("A new offer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OfferDTO result = offerService.save(offerDTO);
        return ResponseEntity
            .created(new URI("/api/employee-mgt/offers/multipart" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /offers} : Updates an existing offer.
     *
     * @param offerDTO the offerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated offerDTO,
     * or with status {@code 400 (Bad Request)} if the offerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the offerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/offers")
    public ResponseEntity<OfferDTO> updateOffer(@Valid @RequestBody OfferDTO offerDTO) throws URISyntaxException {
        log.debug("REST request to update Offer : {}", offerDTO);
        if (offerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OfferDTO result = offerService.save(offerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, offerDTO.getId().toString()))
            .body(result);
    }

    @PutMapping("/offers/multipart")
    public ResponseEntity<OfferDTO> updateOfferMultipart(
        @RequestPart(name = "file") MultipartFile file,
        @RequestPart(name = "offer") OfferDTO offerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Offer with multipart data: {}", offerDTO);

        File savedFile = offerFileService.save(file);
        offerDTO.setImagePath(savedFile.getAbsolutePath());

        if (offerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OfferDTO result = offerService.save(offerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /offers} : get all the offers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of offers in body.
     */
    @GetMapping("/offers")
    public ResponseEntity<List<OfferDTO>> getAllOffers(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Offers");
        Page<OfferDTO> page = offerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /offers/:id} : get the "id" offer.
     *
     * @param id the id of the offerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the offerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/offers/{id}")
    public ResponseEntity<OfferDTO> getOffer(@PathVariable Long id) {
        log.debug("REST request to get Offer : {}", id);
        Optional<OfferDTO> offerDTO = offerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(offerDTO);
    }

    /**
     * {@code DELETE  /offers/:id} : delete the "id" offer.
     *
     * @param id the id of the offerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/offers/{id}")
    public ResponseEntity<Void> deleteOffer(@PathVariable Long id) {
        log.debug("REST request to delete Offer : {}", id);
        offerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
