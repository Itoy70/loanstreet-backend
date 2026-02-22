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

import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@RestController
public class LoanController implements LoansApi {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @Override
    public ResponseEntity<Loan> createLoan(LoanCreateRequest request, UUID xRequestID) {
        Loan created = loanService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<Loan> getLoan(UUID id, UUID xRequestID) {
        Loan loan = loanService.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Loan not found: " + id));
        return ResponseEntity.ok(loan);
    }

    @Override
    public ResponseEntity<Loan> updateLoan(UUID id, LoanUpdateRequest request, UUID xRequestID) {
        Loan loan = loanService.update(id, request)
                .orElseThrow(() -> new NoSuchElementException("Loan not found for update: " + id));
        return ResponseEntity.ok(loan);
    }
}
