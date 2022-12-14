package com.facturation.repository;

import com.facturation.domain.Api;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Api entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApiRepository extends MongoRepository<Api, String> {}
