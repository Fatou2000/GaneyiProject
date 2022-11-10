package com.facturation.web.rest;

import com.facturation.domain.Pricing;
import com.facturation.repository.PricingRepository;
import com.facturation.service.PricingService;
import com.facturation.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.facturation.domain.Pricing}.
 */
@RestController
@RequestMapping("/api")
public class PricingResource {

    private final Logger log = LoggerFactory.getLogger(PricingResource.class);

    private static final String ENTITY_NAME = "pricing";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PricingService pricingService;

    private final PricingRepository pricingRepository;

    public PricingResource(PricingService pricingService, PricingRepository pricingRepository) {
        this.pricingService = pricingService;
        this.pricingRepository = pricingRepository;
    }

    /**
     * {@code POST  /pricings} : Create a new pricing.
     *
     * @param pricing the pricing to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pricing, or with status {@code 400 (Bad Request)} if the pricing has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pricings")
    public ResponseEntity<Pricing> createPricing(@RequestBody Pricing pricing) throws URISyntaxException {
        log.debug("REST request to save Pricing : {}", pricing);
        if (pricing.getId() != null) {
            throw new BadRequestAlertException("A new pricing cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pricing result = pricingService.save(pricing);
        return ResponseEntity
            .created(new URI("/api/pricings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /pricings/:id} : Updates an existing pricing.
     *
     * @param id the id of the pricing to save.
     * @param pricing the pricing to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pricing,
     * or with status {@code 400 (Bad Request)} if the pricing is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pricing couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pricings/{id}")
    public ResponseEntity<Pricing> updatePricing(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Pricing pricing
    ) throws URISyntaxException {
        log.debug("REST request to update Pricing : {}, {}", id, pricing);
        if (pricing.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pricing.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pricingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Pricing result = pricingService.update(pricing);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pricing.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /pricings/:id} : Partial updates given fields of an existing pricing, field will ignore if it is null
     *
     * @param id the id of the pricing to save.
     * @param pricing the pricing to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pricing,
     * or with status {@code 400 (Bad Request)} if the pricing is not valid,
     * or with status {@code 404 (Not Found)} if the pricing is not found,
     * or with status {@code 500 (Internal Server Error)} if the pricing couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pricings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Pricing> partialUpdatePricing(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Pricing pricing
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pricing partially : {}, {}", id, pricing);
        if (pricing.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pricing.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pricingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Pricing> result = pricingService.partialUpdate(pricing);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pricing.getId()));
    }

    /**
     * {@code GET  /pricings} : get all the pricings.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pricings in body.
     */
    @GetMapping("/pricings")
    public ResponseEntity<List<Pricing>> getAllPricings(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Pricings");
        Page<Pricing> page = pricingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pricings/:id} : get the "id" pricing.
     *
     * @param id the id of the pricing to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pricing, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pricings/{id}")
    public ResponseEntity<Pricing> getPricing(@PathVariable String id) {
        log.debug("REST request to get Pricing : {}", id);
        Optional<Pricing> pricing = pricingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pricing);
    }

    /**
     * {@code DELETE  /pricings/:id} : delete the "id" pricing.
     *
     * @param id the id of the pricing to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pricings/{id}")
    public ResponseEntity<Void> deletePricing(@PathVariable String id) {
        log.debug("REST request to delete Pricing : {}", id);
        pricingService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
