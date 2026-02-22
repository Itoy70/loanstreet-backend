package com.loanstreet.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loanstreet.backend.dto.Loan;
import com.loanstreet.backend.dto.LoanCreateRequest;
import com.loanstreet.backend.dto.LoanUpdateRequest;
import com.loanstreet.backend.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoanService loanService;

    @Autowired
    private ObjectMapper objectMapper;

    private Loan testLoanDto;
    private UUID testLoanId;

    private static final String REQUEST_ID_HEADER = "X-Request-ID";

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

        String requestId = UUID.randomUUID().toString();
        LoanCreateRequest request = new LoanCreateRequest()
                .amount(new BigDecimal("10000.00"))
                .interestRate(new BigDecimal("0.05"))
                .lengthInMonths(60)
                .monthlyPaymentAmount(new BigDecimal("188.71"));

        mockMvc.perform(post("/api/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .header(REQUEST_ID_HEADER, requestId)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(REQUEST_ID_HEADER, requestId))
                .andExpect(jsonPath("$.id").value(testLoanId.toString()))
                .andExpect(jsonPath("$.amount").value(10000.00))
                .andExpect(jsonPath("$.interestRate").value(0.05))
                .andExpect(jsonPath("$.lengthInMonths").value(60))
                .andExpect(jsonPath("$.monthlyPaymentAmount").value(188.71));
    }

    @ParameterizedTest
    @CsvSource({
            "-1, 0.05, 60, 188.71, amount",
            "-1000, 0.05, 60, 188.71, amount",
            "10000, 0, 60, 188.71, interestRate",
            "10000, -0.01, 60, 188.71, interestRate",
            "10000, 1.5, 60, 188.71, interestRate",
            "10000, 0.05, 0, 188.71, lengthInMonths",
            "10000, 0.05, 601, 188.71, lengthInMonths",
            "10000, 0.05, -1, 188.71, lengthInMonths",
            "10000, 0.05, 60, 0, monthlyPaymentAmount",
            "10000, 0.05, 60, -50, monthlyPaymentAmount"
    })
    void testCreateLoan_ValidationErrors(BigDecimal amount, BigDecimal interestRate,
                                         Integer lengthInMonths, BigDecimal monthlyPayment, String expectedField) throws Exception {
        LoanCreateRequest request = new LoanCreateRequest()
                .amount(amount)
                .interestRate(interestRate)
                .lengthInMonths(lengthInMonths)
                .monthlyPaymentAmount(monthlyPayment);

        mockMvc.perform(post("/api/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(header().exists(REQUEST_ID_HEADER))
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.errors[0].field").value(expectedField));
    }

    @Test
    void testCreateLoan_NullAmount() throws Exception {
        String json = "{\"interestRate\": 0.05, \"lengthInMonths\": 60, \"monthlyPaymentAmount\": 188.71}";

        mockMvc.perform(post("/api/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(header().exists(REQUEST_ID_HEADER))
                .andExpect(jsonPath("$.error").value("Validation failed"));
    }

    @Test
    void testGetLoan_Success() throws Exception {
        when(loanService.findById(testLoanId)).thenReturn(Optional.of(testLoanDto));

        String requestId = UUID.randomUUID().toString();

        mockMvc.perform(get("/api/loans/{id}", testLoanId)
                .header(REQUEST_ID_HEADER, requestId))
                .andExpect(status().isOk())
                .andExpect(header().string(REQUEST_ID_HEADER, requestId))
                .andExpect(jsonPath("$.id").value(testLoanId.toString()))
                .andExpect(jsonPath("$.amount").value(10000.00));
    }

    @Test
    void testGetLoan_NotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(loanService.findById(randomId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/loans/{id}", randomId))
                .andExpect(status().isNotFound())
                .andExpect(header().exists(REQUEST_ID_HEADER));
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid-uuid", "123", "not-a-uuid"})
    void testGetLoan_InvalidUuidFormat(String invalidId) throws Exception {
        mockMvc.perform(get("/api/loans/{id}", invalidId))
                .andExpect(status().isBadRequest())
                .andExpect(header().exists(REQUEST_ID_HEADER));
    }

    @Test
    void testGetLoan_InvalidRequestIdHeader() throws Exception {
        // Invalid UUID in header returns 400 because Spring validates the UUID type
        // per the OpenAPI spec (format: uuid)
        mockMvc.perform(get("/api/loans/{id}", testLoanId)
                .header(REQUEST_ID_HEADER, "not-a-valid-uuid"))
                .andExpect(status().isBadRequest())
                .andExpect(header().exists(REQUEST_ID_HEADER));
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

        String requestId = UUID.randomUUID().toString();
        LoanUpdateRequest request = new LoanUpdateRequest()
                .amount(new BigDecimal("15000.00"))
                .interestRate(new BigDecimal("0.05"))
                .lengthInMonths(72)
                .monthlyPaymentAmount(new BigDecimal("220.00"));

        mockMvc.perform(put("/api/loans/{id}", testLoanId)
                .contentType(MediaType.APPLICATION_JSON)
                .header(REQUEST_ID_HEADER, requestId)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(header().string(REQUEST_ID_HEADER, requestId))
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
                .andExpect(status().isNotFound())
                .andExpect(header().exists(REQUEST_ID_HEADER));
    }

    @ParameterizedTest
    @CsvSource({
            "-1, 0.05, 60, 188.71, amount",
            "-1000, 0.05, 60, 188.71, amount",
            "10000, 0, 60, 188.71, interestRate",
            "10000, -0.01, 60, 188.71, interestRate",
            "10000, 1.5, 60, 188.71, interestRate",
            "10000, 0.05, 0, 188.71, lengthInMonths",
            "10000, 0.05, 601, 188.71, lengthInMonths",
            "10000, 0.05, 60, -1, monthlyPaymentAmount",
            "10000, 0.05, 60, -50, monthlyPaymentAmount"
    })
    void testUpdateLoan_ValidationErrors(BigDecimal amount, BigDecimal interestRate,
                                         Integer lengthInMonths, BigDecimal monthlyPayment, String expectedField) throws Exception {
        LoanUpdateRequest request = new LoanUpdateRequest()
                .amount(amount)
                .interestRate(interestRate)
                .lengthInMonths(lengthInMonths)
                .monthlyPaymentAmount(monthlyPayment);

        mockMvc.perform(put("/api/loans/{id}", testLoanId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(header().exists(REQUEST_ID_HEADER))
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.errors[0].field").value(expectedField));
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid-uuid", "123", "not-a-uuid"})
    void testUpdateLoan_InvalidUuidFormat(String invalidId) throws Exception {
        LoanUpdateRequest request = new LoanUpdateRequest()
                .amount(new BigDecimal("15000.00"))
                .interestRate(new BigDecimal("0.05"))
                .lengthInMonths(60)
                .monthlyPaymentAmount(new BigDecimal("188.71"));

        mockMvc.perform(put("/api/loans/{id}", invalidId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(header().exists(REQUEST_ID_HEADER));
    }

    @Test
    void testUpdateLoan_EmptyBody() throws Exception {
        mockMvc.perform(put("/api/loans/{id}", testLoanId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(header().exists(REQUEST_ID_HEADER));
    }
}
