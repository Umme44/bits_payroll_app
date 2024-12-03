package com.bits.hr.web.rest;

import com.bits.hr.service.OfferService;
import com.bits.hr.service.dto.OfferDTO;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@Log4j2
@RequestMapping("/api/common/offers")
public class OfferCommonResource {

    @Autowired
    OfferService offerService;

    @GetMapping("/recent-list")
    public ResponseEntity<List<OfferDTO>> recentOffers() {
        log.debug("REST request to get recent offers");
        List<OfferDTO> result = offerService.recentOffer();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/archive-list")
    public ResponseEntity<List<OfferDTO>> archiveOffers(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get recent offers");
        Page<OfferDTO> result = offerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), result);
        return ResponseEntity.ok().headers(headers).body(result.getContent());
    }
}
