package com.facturation.repository;

import com.facturation.domain.ProductLicense;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the ProductLicense entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductLicenseRepository extends MongoRepository<ProductLicense, String> {}
