package com.loanstreet.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loanstreet.backend.dto.Loan;
import com.loanstreet.backend.dto.LoanCreateRequest;
import com.loanstreet.backend.dto.LoanUpdateRequest;
import com.loanstreet.backend.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoanController.class)
public class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoanService loanService;

    @Autowired
    private ObjectMapper objectMapper;

    private Loan testLoanDto;
    private UUID testLoanId;

    @BeforeEach
    void setUp() {
        testLoanId = UUID.randomUUID();
        testLoanDto = new Loan()
                .id(testLoanId)
                .amount(new BigDecimal("10000.00"))
                .interestRate(new BigDecimal("0.05"))
                .lengthInMonths(60)
                .monthlyPaymentAmount(new BigDecimal("188.71"));
    }

    @Test
    void testCreateLoan() throws Exception {
        when(loanService.create(any(LoanCreateRequest.class))).thenReturn(testLoanDto);

        LoanCreateRequest request = new LoanCreateRequest()
                .amount(new BigDecimal("10000.00"))
                .interestRate(new BigDecimal("0.05"))
                .lengthInMonths(60)
                .monthlyPaymentAmount(new BigDecimal("188.71"));

        mockMvc.perform(post("/api/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testLoanId.toString()))
                .andExpect(jsonPath("$.amount").value(10000.00))
                .andExpect(jsonPath("$.interestRate").value(0.05))
                .andExpect(jsonPath("$.lengthInMonths").value(60))
                .andExpect(jsonPath("$.monthlyPaymentAmount").value(188.71));
    }

    @Test
    void testGetLoan_Success() throws Exception {
        when(loanService.findById(testLoanId)).thenReturn(Optional.of(testLoanDto));

        mockMvc.perform(get("/api/loans/{id}", testLoanId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testLoanId.toString()))
                .andExpect(jsonPath("$.amount").value(10000.00));
    }

    @Test
    void testGetLoan_NotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(loanService.findById(randomId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/loans/{id}", randomId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateLoan_Success() throws Exception {
        Loan updatedDto = new Loan()
                .id(testLoanId)
                .amount(new BigDecimal("15000.00"))
                .interestRate(new BigDecimal("0.05"))
                .lengthInMonths(72)
                .monthlyPaymentAmount(new BigDecimal("220.00"));

        when(loanService.update(eq(testLoanId), any(LoanUpdateRequest.class)))
                .thenReturn(Optional.of(updatedDto));

        LoanUpdateRequest request = new LoanUpdateRequest()
                .amount(new BigDecimal("15000.00"))
                .interestRate(new BigDecimal("0.05"))
                .lengthInMonths(72)
                .monthlyPaymentAmount(new BigDecimal("220.00"));

        mockMvc.perform(put("/api/loans/{id}", testLoanId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testLoanId.toString()))
                .andExpect(jsonPath("$.amount").value(15000.00))
                .andExpect(jsonPath("$.lengthInMonths").value(72));
    }

    @Test
    void testUpdateLoan_NotFound() throws Exception {
        UUID randomId = UUID.randomUUID();

        when(loanService.update(eq(randomId), any(LoanUpdateRequest.class)))
                .thenReturn(Optional.empty());

        LoanUpdateRequest request = new LoanUpdateRequest()
                .amount(new BigDecimal("15000.00"))
                .interestRate(new BigDecimal("0.05"))
                .lengthInMonths(60)
                .monthlyPaymentAmount(new BigDecimal("188.71"));

        mockMvc.perform(put("/api/loans/{id}", randomId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}
