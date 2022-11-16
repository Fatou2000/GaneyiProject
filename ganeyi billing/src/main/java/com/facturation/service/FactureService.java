package com.facturation.service;

import com.facturation.domain.Facture;
import com.facturation.domain.Client;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Facture}.
 */
public interface FactureService {
    /**
     * Save a facture.
     *
     * @param facture the entity to save.
     * @return the persisted entity.
     */
    Facture save(Facture facture);

    /**
     * Updates a facture.
     *
     * @param facture the entity to update.
     * @return the persisted entity.
     */
    Facture update(Facture facture);

    /**
     * Partially updates a facture.
     *
     * @param facture the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Facture> partialUpdate(Facture facture);

    /**
     * Get all the factures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Facture> findAll(Pageable pageable);
    /**
     * Get all the Facture where Payment is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Facture> findAllWherePaymentIsNull();

    /**
     * Get all the factures with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Facture> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" facture.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Facture> findOne(String id);

    /**
     * Delete the "id" facture.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    Facture findByClient(Client client);

    Facture findbillingbyname(String firstname);
}
