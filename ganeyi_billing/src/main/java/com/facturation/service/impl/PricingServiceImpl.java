package com.facturation.service.impl;

import com.facturation.domain.Pricing;
import com.facturation.repository.PricingRepository;
import com.facturation.service.PricingService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Pricing}.
 */
@Service
public class PricingServiceImpl implements PricingService {

    private final Logger log = LoggerFactory.getLogger(PricingServiceImpl.class);

    private final PricingRepository pricingRepository;

    public PricingServiceImpl(PricingRepository pricingRepository) {
        this.pricingRepository = pricingRepository;
    }

    @Override
    public Pricing save(Pricing pricing) {
        log.debug("Request to save Pricing : {}", pricing);
        return pricingRepository.save(pricing);
    }

    @Override
    public Pricing update(Pricing pricing) {
        log.debug("Request to update Pricing : {}", pricing);
        return pricingRepository.save(pricing);
    }

    @Override
    public Optional<Pricing> partialUpdate(Pricing pricing) {
        log.debug("Request to partially update Pricing : {}", pricing);

        return pricingRepository
            .findById(pricing.getId())
            .map(existingPricing -> {
                if (pricing.getValue() != null) {
                    existingPricing.setValue(pricing.getValue());
                }

                return existingPricing;
            })
            .map(pricingRepository::save);
    }

    @Override
    public Page<Pricing> findAll(Pageable pageable) {
        log.debug("Request to get all Pricings");
        return pricingRepository.findAll(pageable);
    }

    @Override
    public Optional<Pricing> findOne(String id) {
        log.debug("Request to get Pricing : {}", id);
        return pricingRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Pricing : {}", id);
        pricingRepository.deleteById(id);
    }
}
