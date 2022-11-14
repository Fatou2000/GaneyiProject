package com.facturation.service;

import com.facturation.domain.Forfait;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Forfait}.
 */
public interface ForfaitService {
    /**
     * Save a forfait.
     *
     * @param forfait the entity to save.
     * @return the persisted entity.
     */
    Forfait save(Forfait forfait);

    /**
     * Updates a forfait.
     *
     * @param forfait the entity to update.
     * @return the persisted entity.
     */
    Forfait update(Forfait forfait);

    /**
     * Partially updates a forfait.
     *
     * @param forfait the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Forfait> partialUpdate(Forfait forfait);

    /**
     * Get all the forfaits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Forfait> findAll(Pageable pageable);
    /**
     * Get all the Forfait where Facture is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Forfait> findAllWhereFactureIsNull();

    /**
     * Get the "id" forfait.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Forfait> findOne(String id);

    /**
     * Delete the "id" forfait.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
