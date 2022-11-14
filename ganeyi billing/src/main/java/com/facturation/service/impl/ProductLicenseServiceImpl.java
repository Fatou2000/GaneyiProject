package com.facturation.service.impl;

import com.facturation.domain.ProductLicense;
import com.facturation.repository.ProductLicenseRepository;
import com.facturation.service.ProductLicenseService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link ProductLicense}.
 */
@Service
public class ProductLicenseServiceImpl implements ProductLicenseService {

    private final Logger log = LoggerFactory.getLogger(ProductLicenseServiceImpl.class);

    private final ProductLicenseRepository productLicenseRepository;

    public ProductLicenseServiceImpl(ProductLicenseRepository productLicenseRepository) {
        this.productLicenseRepository = productLicenseRepository;
    }

    @Override
    public ProductLicense save(ProductLicense productLicense) {
        log.debug("Request to save ProductLicense : {}", productLicense);
        return productLicenseRepository.save(productLicense);
    }

    @Override
    public ProductLicense update(ProductLicense productLicense) {
        log.debug("Request to update ProductLicense : {}", productLicense);
        return productLicenseRepository.save(productLicense);
    }

    @Override
    public Optional<ProductLicense> partialUpdate(ProductLicense productLicense) {
        log.debug("Request to partially update ProductLicense : {}", productLicense);

        return productLicenseRepository
            .findById(productLicense.getId())
            .map(existingProductLicense -> {
                if (productLicense.getAccessKey() != null) {
                    existingProductLicense.setAccessKey(productLicense.getAccessKey());
                }
                if (productLicense.getStartDate() != null) {
                    existingProductLicense.setStartDate(productLicense.getStartDate());
                }
                if (productLicense.getEndDate() != null) {
                    existingProductLicense.setEndDate(productLicense.getEndDate());
                }
                if (productLicense.getIsActive() != null) {
                    existingProductLicense.setIsActive(productLicense.getIsActive());
                }

                return existingProductLicense;
            })
            .map(productLicenseRepository::save);
    }

    @Override
    public Page<ProductLicense> findAll(Pageable pageable) {
        log.debug("Request to get all ProductLicenses");
        return productLicenseRepository.findAll(pageable);
    }

    @Override
    public Optional<ProductLicense> findOne(String id) {
        log.debug("Request to get ProductLicense : {}", id);
        return productLicenseRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete ProductLicense : {}", id);
        productLicenseRepository.deleteById(id);
    }
}
