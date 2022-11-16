package com.facturation.repository;

import com.facturation.domain.Facture;
import com.facturation.domain.Client;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Facture entity.
 */
@Repository
public interface FactureRepository extends MongoRepository<Facture, String> {
    @Query("{}")
    Page<Facture> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<Facture> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<Facture> findOneWithEagerRelationships(String id);

    @Query("{}")
    Facture findByClient(Client client);
}
