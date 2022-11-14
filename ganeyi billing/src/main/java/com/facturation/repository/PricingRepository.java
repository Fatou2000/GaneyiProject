package com.facturation.repository;

import com.facturation.domain.Pricing;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Pricing entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PricingRepository extends MongoRepository<Pricing, String> {}
