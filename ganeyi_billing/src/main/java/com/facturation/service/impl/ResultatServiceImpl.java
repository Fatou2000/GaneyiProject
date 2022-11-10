package com.facturation.service.impl;

import com.facturation.domain.Resultat;
import com.facturation.repository.ResultatRepository;
import com.facturation.service.ResultatService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Resultat}.
 */
@Service
public class ResultatServiceImpl implements ResultatService {

    private final Logger log = LoggerFactory.getLogger(ResultatServiceImpl.class);

    private final ResultatRepository resultatRepository;

    public ResultatServiceImpl(ResultatRepository resultatRepository) {
        this.resultatRepository = resultatRepository;
    }

    @Override
    public Resultat save(Resultat resultat) {
        log.debug("Request to save Resultat : {}", resultat);
        return resultatRepository.save(resultat);
    }

    @Override
    public Resultat update(Resultat resultat) {
        log.debug("Request to update Resultat : {}", resultat);
        return resultatRepository.save(resultat);
    }

    @Override
    public Optional<Resultat> partialUpdate(Resultat resultat) {
        log.debug("Request to partially update Resultat : {}", resultat);

        return resultatRepository
            .findById(resultat.getId())
            .map(existingResultat -> {
                if (resultat.getValue() != null) {
                    existingResultat.setValue(resultat.getValue());
                }
                if (resultat.getValueContentType() != null) {
                    existingResultat.setValueContentType(resultat.getValueContentType());
                }

                return existingResultat;
            })
            .map(resultatRepository::save);
    }

    @Override
    public Page<Resultat> findAll(Pageable pageable) {
        log.debug("Request to get all Resultats");
        return resultatRepository.findAll(pageable);
    }

    /**
     *  Get all the resultats where Request is {@code null}.
     *  @return the list of entities.
     */

    public List<Resultat> findAllWhereRequestIsNull() {
        log.debug("Request to get all resultats where Request is null");
        return StreamSupport
            .stream(resultatRepository.findAll().spliterator(), false)
            .filter(resultat -> resultat.getRequest() == null)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Resultat> findOne(String id) {
        log.debug("Request to get Resultat : {}", id);
        return resultatRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Resultat : {}", id);
        resultatRepository.deleteById(id);
    }
}
