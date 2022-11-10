package com.facturation.web.rest;

import com.facturation.domain.ProductLicense;
import com.facturation.repository.ProductLicenseRepository;
import com.facturation.service.ProductLicenseService;
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
 * REST controller for managing {@link com.facturation.domain.ProductLicense}.
 */
@RestController
@RequestMapping("/api")
public class ProductLicenseResource {

    private final Logger log = LoggerFactory.getLogger(ProductLicenseResource.class);

    private static final String ENTITY_NAME = "productLicense";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductLicenseService productLicenseService;

    private final ProductLicenseRepository productLicenseRepository;

    public ProductLicenseResource(ProductLicenseService productLicenseService, ProductLicenseRepository productLicenseRepository) {
        this.productLicenseService = productLicenseService;
        this.productLicenseRepository = productLicenseRepository;
    }

    /**
     * {@code POST  /product-licenses} : Create a new productLicense.
     *
     * @param productLicense the productLicense to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productLicense, or with status {@code 400 (Bad Request)} if the productLicense has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-licenses")
    public ResponseEntity<ProductLicense> createProductLicense(@RequestBody ProductLicense productLicense) throws URISyntaxException {
        log.debug("REST request to save ProductLicense : {}", productLicense);
        if (productLicense.getId() != null) {
            throw new BadRequestAlertException("A new productLicense cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductLicense result = productLicenseService.save(productLicense);
        return ResponseEntity
            .created(new URI("/api/product-licenses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /product-licenses/:id} : Updates an existing productLicense.
     *
     * @param id the id of the productLicense to save.
     * @param productLicense the productLicense to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productLicense,
     * or with status {@code 400 (Bad Request)} if the productLicense is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productLicense couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-licenses/{id}")
    public ResponseEntity<ProductLicense> updateProductLicense(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ProductLicense productLicense
    ) throws URISyntaxException {
        log.debug("REST request to update ProductLicense : {}, {}", id, productLicense);
        if (productLicense.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productLicense.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productLicenseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductLicense result = productLicenseService.update(productLicense);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productLicense.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-licenses/:id} : Partial updates given fields of an existing productLicense, field will ignore if it is null
     *
     * @param id the id of the productLicense to save.
     * @param productLicense the productLicense to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productLicense,
     * or with status {@code 400 (Bad Request)} if the productLicense is not valid,
     * or with status {@code 404 (Not Found)} if the productLicense is not found,
     * or with status {@code 500 (Internal Server Error)} if the productLicense couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-licenses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductLicense> partialUpdateProductLicense(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ProductLicense productLicense
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductLicense partially : {}, {}", id, productLicense);
        if (productLicense.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productLicense.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productLicenseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductLicense> result = productLicenseService.partialUpdate(productLicense);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productLicense.getId())
        );
    }

    /**
     * {@code GET  /product-licenses} : get all the productLicenses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productLicenses in body.
     */
    @GetMapping("/product-licenses")
    public ResponseEntity<List<ProductLicense>> getAllProductLicenses(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ProductLicenses");
        Page<ProductLicense> page = productLicenseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-licenses/:id} : get the "id" productLicense.
     *
     * @param id the id of the productLicense to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productLicense, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-licenses/{id}")
    public ResponseEntity<ProductLicense> getProductLicense(@PathVariable String id) {
        log.debug("REST request to get ProductLicense : {}", id);
        Optional<ProductLicense> productLicense = productLicenseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productLicense);
    }

    /**
     * {@code DELETE  /product-licenses/:id} : delete the "id" productLicense.
     *
     * @param id the id of the productLicense to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-licenses/{id}")
    public ResponseEntity<Void> deleteProductLicense(@PathVariable String id) {
        log.debug("REST request to delete ProductLicense : {}", id);
        productLicenseService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
