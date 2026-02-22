package com.loanstreet.backend.service;

import com.loanstreet.backend.dto.Loan;
import com.loanstreet.backend.dto.LoanCreateRequest;
import com.loanstreet.backend.dto.LoanUpdateRequest;
import com.loanstreet.backend.model.LoanEntity;
import com.loanstreet.backend.repository.LoanRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class LoanService {

    private final LoanRepository loanRepository;

    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public Loan create(LoanCreateRequest request) {
        log.info("Creating loan: amount={}, interestRate={}, lengthInMonths={}",
                request.getAmount(), request.getInterestRate(), request.getLengthInMonths());

        LoanEntity entity = new LoanEntity();
        entity.setAmount(request.getAmount());
        entity.setInterestRate(request.getInterestRate());
        entity.setLengthInMonths(request.getLengthInMonths());
        entity.setMonthlyPaymentAmount(request.getMonthlyPaymentAmount());

        LoanEntity saved = loanRepository.save(entity);
        log.info("Loan created with id={}", saved.getId());
        return toDto(saved);
    }

    public Optional<Loan> findById(UUID id) {
        log.debug("Fetching loan id={}", id);
        return loanRepository.findById(id)
                .map(entity -> {
                    log.debug("Loan found id={}", id);
                    return toDto(entity);
                });
    }

    @Transactional
    public Optional<Loan> update(UUID id, LoanUpdateRequest request) {
        log.info("Updating loan id={}", id);
        return loanRepository.findById(id)
                .map(existing -> {
                    existing.setAmount(request.getAmount());
                    existing.setInterestRate(request.getInterestRate());
                    existing.setLengthInMonths(request.getLengthInMonths());
                    existing.setMonthlyPaymentAmount(request.getMonthlyPaymentAmount());
                    LoanEntity updated = loanRepository.save(existing);
                    log.info("Loan updated id={}", updated.getId());
                    return toDto(updated);
                });
    }

    private Loan toDto(LoanEntity entity) {
        return new Loan()
                .id(entity.getId())
                .amount(entity.getAmount())
                .interestRate(entity.getInterestRate())
                .lengthInMonths(entity.getLengthInMonths())
                .monthlyPaymentAmount(entity.getMonthlyPaymentAmount());
    }
}
