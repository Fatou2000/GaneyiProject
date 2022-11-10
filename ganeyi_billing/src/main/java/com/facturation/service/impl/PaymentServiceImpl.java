package com.facturation.service.impl;

import com.facturation.domain.Payment;
import com.facturation.repository.PaymentRepository;
import com.facturation.service.PaymentService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Payment}.
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    private final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment save(Payment payment) {
        log.debug("Request to save Payment : {}", payment);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment update(Payment payment) {
        log.debug("Request to update Payment : {}", payment);
        return paymentRepository.save(payment);
    }

    @Override
    public Optional<Payment> partialUpdate(Payment payment) {
        log.debug("Request to partially update Payment : {}", payment);

        return paymentRepository
            .findById(payment.getId())
            .map(existingPayment -> {
                if (payment.getReference() != null) {
                    existingPayment.setReference(payment.getReference());
                }
                if (payment.getAmount() != null) {
                    existingPayment.setAmount(payment.getAmount());
                }
                if (payment.getType() != null) {
                    existingPayment.setType(payment.getType());
                }
                if (payment.getStatus() != null) {
                    existingPayment.setStatus(payment.getStatus());
                }
                if (payment.getPaidAt() != null) {
                    existingPayment.setPaidAt(payment.getPaidAt());
                }

                return existingPayment;
            })
            .map(paymentRepository::save);
    }

    @Override
    public Page<Payment> findAll(Pageable pageable) {
        log.debug("Request to get all Payments");
        return paymentRepository.findAll(pageable);
    }

    @Override
    public Optional<Payment> findOne(String id) {
        log.debug("Request to get Payment : {}", id);
        return paymentRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Payment : {}", id);
        paymentRepository.deleteById(id);
    }
}
