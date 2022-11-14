package com.facturation.service;

import com.facturation.domain.Api;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Api}.
 */
public interface ApiService {
    /**
     * Save a api.
     *
     * @param api the entity to save.
     * @return the persisted entity.
     */
    Api save(Api api);

    /**
     * Updates a api.
     *
     * @param api the entity to update.
     * @return the persisted entity.
     */
    Api update(Api api);

    /**
     * Partially updates a api.
     *
     * @param api the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Api> partialUpdate(Api api);

    /**
     * Get all the apis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Api> findAll(Pageable pageable);

    /**
     * Get the "id" api.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Api> findOne(String id);

    /**
     * Delete the "id" api.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
