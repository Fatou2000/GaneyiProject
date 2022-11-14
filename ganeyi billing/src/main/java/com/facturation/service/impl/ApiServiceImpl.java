package com.facturation.service.impl;

import com.facturation.domain.Api;
import com.facturation.repository.ApiRepository;
import com.facturation.service.ApiService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Api}.
 */
@Service
public class ApiServiceImpl implements ApiService {

    private final Logger log = LoggerFactory.getLogger(ApiServiceImpl.class);

    private final ApiRepository apiRepository;

    public ApiServiceImpl(ApiRepository apiRepository) {
        this.apiRepository = apiRepository;
    }

    @Override
    public Api save(Api api) {
        log.debug("Request to save Api : {}", api);
        return apiRepository.save(api);
    }

    @Override
    public Api update(Api api) {
        log.debug("Request to update Api : {}", api);
        return apiRepository.save(api);
    }

    @Override
    public Optional<Api> partialUpdate(Api api) {
        log.debug("Request to partially update Api : {}", api);

        return apiRepository
            .findById(api.getId())
            .map(existingApi -> {
                if (api.getVersion() != null) {
                    existingApi.setVersion(api.getVersion());
                }
                if (api.getServiceURL() != null) {
                    existingApi.setServiceURL(api.getServiceURL());
                }
                if (api.getDocURL() != null) {
                    existingApi.setDocURL(api.getDocURL());
                }
                if (api.getIsActice() != null) {
                    existingApi.setIsActice(api.getIsActice());
                }

                return existingApi;
            })
            .map(apiRepository::save);
    }

    @Override
    public Page<Api> findAll(Pageable pageable) {
        log.debug("Request to get all Apis");
        return apiRepository.findAll(pageable);
    }

    @Override
    public Optional<Api> findOne(String id) {
        log.debug("Request to get Api : {}", id);
        return apiRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Api : {}", id);
        apiRepository.deleteById(id);
    }
}
