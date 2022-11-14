package com.facturation.service.impl;

import com.facturation.domain.Forfait;
import com.facturation.repository.ForfaitRepository;
import com.facturation.service.ForfaitService;
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
 * Service Implementation for managing {@link Forfait}.
 */
@Service
public class ForfaitServiceImpl implements ForfaitService {

    private final Logger log = LoggerFactory.getLogger(ForfaitServiceImpl.class);

    private final ForfaitRepository forfaitRepository;

    public ForfaitServiceImpl(ForfaitRepository forfaitRepository) {
        this.forfaitRepository = forfaitRepository;
    }

    @Override
    public Forfait save(Forfait forfait) {
        log.debug("Request to save Forfait : {}", forfait);
        return forfaitRepository.save(forfait);
    }

    @Override
    public Forfait update(Forfait forfait) {
        log.debug("Request to update Forfait : {}", forfait);
        return forfaitRepository.save(forfait);
    }

    @Override
    public Optional<Forfait> partialUpdate(Forfait forfait) {
        log.debug("Request to partially update Forfait : {}", forfait);

        return forfaitRepository
            .findById(forfait.getId())
            .map(existingForfait -> {
                if (forfait.getNom() != null) {
                    existingForfait.setNom(forfait.getNom());
                }
                if (forfait.getDescription() != null) {
                    existingForfait.setDescription(forfait.getDescription());
                }
                if (forfait.getNumberOfQueries() != null) {
                    existingForfait.setNumberOfQueries(forfait.getNumberOfQueries());
                }
                if (forfait.getPrice() != null) {
                    existingForfait.setPrice(forfait.getPrice());
                }
                if (forfait.getPeriode() != null) {
                    existingForfait.setPeriode(forfait.getPeriode());
                }
                if (forfait.getActif() != null) {
                    existingForfait.setActif(forfait.getActif());
                }

                return existingForfait;
            })
            .map(forfaitRepository::save);
    }

    @Override
    public Page<Forfait> findAll(Pageable pageable) {
        log.debug("Request to get all Forfaits");
        return forfaitRepository.findAll(pageable);
    }

    /**
     *  Get all the forfaits where Facture is {@code null}.
     *  @return the list of entities.
     */

    public List<Forfait> findAllWhereFactureIsNull() {
        log.debug("Request to get all forfaits where Facture is null");
        return StreamSupport
            .stream(forfaitRepository.findAll().spliterator(), false)
            .filter(forfait -> forfait.getFacture() == null)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Forfait> findOne(String id) {
        log.debug("Request to get Forfait : {}", id);
        return forfaitRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Forfait : {}", id);
        forfaitRepository.deleteById(id);
    }
}
