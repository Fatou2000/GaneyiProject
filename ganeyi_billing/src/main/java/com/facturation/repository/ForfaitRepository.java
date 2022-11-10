package com.facturation.repository;

import com.facturation.domain.Forfait;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Forfait entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ForfaitRepository extends MongoRepository<Forfait, String> {}
