package com.facturation.service;

import com.facturation.domain.ProductLicense;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ProductLicense}.
 */
public interface ProductLicenseService {
    /**
     * Save a productLicense.
     *
     * @param productLicense the entity to save.
     * @return the persisted entity.
     */
    ProductLicense save(ProductLicense productLicense);

    /**
     * Updates a productLicense.
     *
     * @param productLicense the entity to update.
     * @return the persisted entity.
     */
    ProductLicense update(ProductLicense productLicense);

    /**
     * Partially updates a productLicense.
     *
     * @param productLicense the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductLicense> partialUpdate(ProductLicense productLicense);

    /**
     * Get all the productLicenses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductLicense> findAll(Pageable pageable);

    /**
     * Get the "id" productLicense.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductLicense> findOne(String id);

    /**
     * Delete the "id" productLicense.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
