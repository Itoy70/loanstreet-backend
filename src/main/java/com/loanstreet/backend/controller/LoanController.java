package com.loanstreet.backend.controller;

import com.loanstreet.backend.api.LoansApi;
import com.loanstreet.backend.dto.Loan;
import com.loanstreet.backend.dto.LoanCreateRequest;
import com.loanstreet.backend.dto.LoanUpdateRequest;
import com.loanstreet.backend.service.LoanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
public class LoanController implements LoansApi {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @Override
    public ResponseEntity<Loan> createLoan(LoanCreateRequest request) {
        Loan created = loanService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<Loan> getLoan(UUID id) {
        return loanService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Loan not found id={}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @Override
    public ResponseEntity<Loan> updateLoan(UUID id, LoanUpdateRequest request) {
        return loanService.update(id, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Loan not found for update id={}", id);
                    return ResponseEntity.notFound().build();
                });
    }
}
