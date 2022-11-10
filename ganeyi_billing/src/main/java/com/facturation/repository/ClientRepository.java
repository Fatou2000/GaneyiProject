package com.facturation.repository;

import com.facturation.domain.Client;
import com.facturation.domain.Facture;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Client entity.
 */
@Repository
public interface ClientRepository extends MongoRepository<Client, String> {
    @Query("{}")
    Page<Client> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<Client> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<Client> findOneWithEagerRelationships(String id);




}
