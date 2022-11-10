package com.facturation.service;

import com.facturation.domain.Pricing;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Pricing}.
 */
public interface PricingService {
    /**
     * Save a pricing.
     *
     * @param pricing the entity to save.
     * @return the persisted entity.
     */
    Pricing save(Pricing pricing);

    /**
     * Updates a pricing.
     *
     * @param pricing the entity to update.
     * @return the persisted entity.
     */
    Pricing update(Pricing pricing);

    /**
     * Partially updates a pricing.
     *
     * @param pricing the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Pricing> partialUpdate(Pricing pricing);

    /**
     * Get all the pricings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Pricing> findAll(Pageable pageable);

    /**
     * Get the "id" pricing.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Pricing> findOne(String id);

    /**
     * Delete the "id" pricing.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
