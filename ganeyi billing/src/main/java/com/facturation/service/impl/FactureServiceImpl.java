package com.facturation.service.impl;

import com.facturation.domain.Facture;
import com.facturation.domain.Client;
import com.facturation.repository.FactureRepository;
import com.facturation.repository.ClientRepository;
import com.facturation.service.FactureService;
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
 * Service Implementation for managing {@link Facture}.
 */
@Service
public class FactureServiceImpl implements FactureService {

    private final Logger log = LoggerFactory.getLogger(FactureServiceImpl.class);

    private final FactureRepository factureRepository;
    private final ClientRepository clientRepository;

    public FactureServiceImpl(FactureRepository factureRepository,ClientRepository clientRepository) {
        this.factureRepository = factureRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public Facture save(Facture facture) {
        log.debug("Request to save Facture : {}", facture);
        return factureRepository.save(facture);
    }

    @Override
    public Facture update(Facture facture) {
        log.debug("Request to update Facture : {}", facture);
        return factureRepository.save(facture);
    }

    @Override
    public Optional<Facture> partialUpdate(Facture facture) {
        log.debug("Request to partially update Facture : {}", facture);

        return factureRepository
            .findById(facture.getId())
            .map(existingFacture -> {
                if (facture.getRabais() != null) {
                    existingFacture.setRabais(facture.getRabais());
                }
                if (facture.getTva() != null) {
                    existingFacture.setTva(facture.getTva());
                }
                if (facture.getSousTotal() != null) {
                    existingFacture.setSousTotal(facture.getSousTotal());
                }
                if (facture.getTotal() != null) {
                    existingFacture.setTotal(facture.getTotal());
                }
                if (facture.getTypeFacturation() != null) {
                    existingFacture.setTypeFacturation(facture.getTypeFacturation());
                }
                if (facture.getStatus() != null) {
                    existingFacture.setStatus(facture.getStatus());
                }
                if (facture.getReference() != null) {
                    existingFacture.setReference(facture.getReference());
                }
                if (facture.getDate() != null) {
                    existingFacture.setDate(facture.getDate());
                }
                if (facture.getNumero() != null) {
                    existingFacture.setNumero(facture.getNumero());
                }

                return existingFacture;
            })
            .map(factureRepository::save);
    }

    @Override
    public Page<Facture> findAll(Pageable pageable) {
        log.debug("Request to get all Factures");
        return factureRepository.findAll(pageable);
    }

    public Page<Facture> findAllWithEagerRelationships(Pageable pageable) {
        return factureRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     *  Get all the factures where Payment is {@code null}.
     *  @return the list of entities.
     */

    public List<Facture> findAllWherePaymentIsNull() {
        log.debug("Request to get all factures where Payment is null");
        return StreamSupport
            .stream(factureRepository.findAll().spliterator(), false)
            .filter(facture -> facture.getPayment() == null)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Facture> findOne(String id) {
        log.debug("Request to get Facture : {}", id);
        return factureRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Facture : {}", id);
        factureRepository.deleteById(id);
    }

    @Override
    public Facture findByClient(Client client){
        log.debug("Request to find client's billing : {}", client);
        return factureRepository.findByClient(client);

    }

    @Override
    public Facture findbillingbyname(String firstname){
        Client client = clientRepository.findByfirstname(firstname);
        Facture facture = factureRepository.findByClient(client);
        return facture;

    }
}
