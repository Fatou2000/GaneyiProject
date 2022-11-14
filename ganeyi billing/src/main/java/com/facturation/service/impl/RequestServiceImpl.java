package com.facturation.service.impl;

import com.facturation.domain.Request;
import com.facturation.repository.RequestRepository;
import com.facturation.service.RequestService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Request}.
 */
@Service
public class RequestServiceImpl implements RequestService {

    private final Logger log = LoggerFactory.getLogger(RequestServiceImpl.class);

    private final RequestRepository requestRepository;

    public RequestServiceImpl(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public Request save(Request request) {
        log.debug("Request to save Request : {}", request);
        return requestRepository.save(request);
    }

    @Override
    public Request update(Request request) {
        log.debug("Request to update Request : {}", request);
        return requestRepository.save(request);
    }

    @Override
    public Optional<Request> partialUpdate(Request request) {
        log.debug("Request to partially update Request : {}", request);

        return requestRepository
            .findById(request.getId())
            .map(existingRequest -> {
                if (request.getDuration() != null) {
                    existingRequest.setDuration(request.getDuration());
                }
                if (request.getStatus() != null) {
                    existingRequest.setStatus(request.getStatus());
                }
                if (request.getRequestDate() != null) {
                    existingRequest.setRequestDate(request.getRequestDate());
                }

                return existingRequest;
            })
            .map(requestRepository::save);
    }

    @Override
    public Page<Request> findAll(Pageable pageable) {
        log.debug("Request to get all Requests");
        return requestRepository.findAll(pageable);
    }

    @Override
    public Optional<Request> findOne(String id) {
        log.debug("Request to get Request : {}", id);
        return requestRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Request : {}", id);
        requestRepository.deleteById(id);
    }
}
