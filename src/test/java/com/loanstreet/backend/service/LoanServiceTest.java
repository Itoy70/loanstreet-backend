package com.loanstreet.backend.service;

import com.loanstreet.backend.dto.Loan;
import com.loanstreet.backend.dto.LoanCreateRequest;
import com.loanstreet.backend.dto.LoanUpdateRequest;
import com.loanstreet.backend.model.LoanEntity;
import com.loanstreet.backend.repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanService loanService;

    private UUID testId;
    private LoanEntity testEntity;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();

        testEntity = new LoanEntity();
        testEntity.setId(testId);
        testEntity.setAmount(new BigDecimal("10000.0000"));
        testEntity.setInterestRate(new BigDecimal("0.050000"));
        testEntity.setLengthInMonths(60);
        testEntity.setMonthlyPaymentAmount(new BigDecimal("188.7100"));
    }

    @Test
    void create_savesEntityAndReturnsDto() {
        LoanCreateRequest request = new LoanCreateRequest()
                .amount(new BigDecimal("10000.00"))
                .interestRate(new BigDecimal("0.05"))
                .lengthInMonths(60)
                .monthlyPaymentAmount(new BigDecimal("188.71"));

        when(loanRepository.save(any(LoanEntity.class))).thenReturn(testEntity);

        Loan result = loanService.create(request);

        assertThat(result.getId()).isEqualTo(testId);
        assertThat(result.getAmount()).isEqualByComparingTo("10000.00");
        assertThat(result.getInterestRate()).isEqualByComparingTo("0.05");
        assertThat(result.getLengthInMonths()).isEqualTo(60);
        assertThat(result.getMonthlyPaymentAmount()).isEqualByComparingTo("188.71");
    }

    @Test
    void create_mapsAllFieldsToEntity() {
        LoanCreateRequest request = new LoanCreateRequest()
                .amount(new BigDecimal("25000.00"))
                .interestRate(new BigDecimal("0.065"))
                .lengthInMonths(360)
                .monthlyPaymentAmount(new BigDecimal("1580.17"));

        when(loanRepository.save(any(LoanEntity.class))).thenReturn(testEntity);

        loanService.create(request);

        ArgumentCaptor<LoanEntity> captor = ArgumentCaptor.forClass(LoanEntity.class);
        verify(loanRepository).save(captor.capture());

        LoanEntity saved = captor.getValue();
        assertThat(saved.getAmount()).isEqualByComparingTo("25000.00");
        assertThat(saved.getInterestRate()).isEqualByComparingTo("0.065");
        assertThat(saved.getLengthInMonths()).isEqualTo(360);
        assertThat(saved.getMonthlyPaymentAmount()).isEqualByComparingTo("1580.17");
        assertThat(saved.getId()).isNull();
    }

    @Test
    void findById_returnsLoanWhenFound() {
        when(loanRepository.findById(testId)).thenReturn(Optional.of(testEntity));

        Optional<Loan> result = loanService.findById(testId);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(testId);
        assertThat(result.get().getAmount()).isEqualByComparingTo("10000.00");
        assertThat(result.get().getInterestRate()).isEqualByComparingTo("0.05");
        assertThat(result.get().getLengthInMonths()).isEqualTo(60);
        assertThat(result.get().getMonthlyPaymentAmount()).isEqualByComparingTo("188.71");
    }

    @Test
    void findById_returnsEmptyWhenNotFound() {
        UUID missingId = UUID.randomUUID();
        when(loanRepository.findById(missingId)).thenReturn(Optional.empty());

        Optional<Loan> result = loanService.findById(missingId);

        assertThat(result).isEmpty();
    }

    @Test
    void update_returnsUpdatedLoanWhenFound() {
        LoanUpdateRequest request = new LoanUpdateRequest()
                .amount(new BigDecimal("15000.00"))
                .interestRate(new BigDecimal("0.045"))
                .lengthInMonths(72)
                .monthlyPaymentAmount(new BigDecimal("220.00"));

        LoanEntity updatedEntity = new LoanEntity();
        updatedEntity.setId(testId);
        updatedEntity.setAmount(new BigDecimal("15000.0000"));
        updatedEntity.setInterestRate(new BigDecimal("0.045000"));
        updatedEntity.setLengthInMonths(72);
        updatedEntity.setMonthlyPaymentAmount(new BigDecimal("220.0000"));

        when(loanRepository.findById(testId)).thenReturn(Optional.of(testEntity));
        when(loanRepository.save(testEntity)).thenReturn(updatedEntity);

        Optional<Loan> result = loanService.update(testId, request);

        assertThat(result).isPresent();
        assertThat(result.get().getAmount()).isEqualByComparingTo("15000.00");
        assertThat(result.get().getInterestRate()).isEqualByComparingTo("0.045");
        assertThat(result.get().getLengthInMonths()).isEqualTo(72);
        assertThat(result.get().getMonthlyPaymentAmount()).isEqualByComparingTo("220.00");
    }

    @Test
    void update_mutatesExistingEntityBeforeSaving() {
        LoanUpdateRequest request = new LoanUpdateRequest()
                .amount(new BigDecimal("15000.00"))
                .interestRate(new BigDecimal("0.045"))
                .lengthInMonths(72)
                .monthlyPaymentAmount(new BigDecimal("220.00"));

        when(loanRepository.findById(testId)).thenReturn(Optional.of(testEntity));
        when(loanRepository.save(any(LoanEntity.class))).thenReturn(testEntity);

        loanService.update(testId, request);

        assertThat(testEntity.getAmount()).isEqualByComparingTo("15000.00");
        assertThat(testEntity.getInterestRate()).isEqualByComparingTo("0.045");
        assertThat(testEntity.getLengthInMonths()).isEqualTo(72);
        assertThat(testEntity.getMonthlyPaymentAmount()).isEqualByComparingTo("220.00");
        verify(loanRepository).save(testEntity);
    }

    @Test
    void update_returnsEmptyWhenNotFound() {
        UUID missingId = UUID.randomUUID();
        LoanUpdateRequest request = new LoanUpdateRequest()
                .amount(new BigDecimal("15000.00"))
                .interestRate(new BigDecimal("0.045"))
                .lengthInMonths(72)
                .monthlyPaymentAmount(new BigDecimal("220.00"));

        when(loanRepository.findById(missingId)).thenReturn(Optional.empty());

        Optional<Loan> result = loanService.update(missingId, request);

        assertThat(result).isEmpty();
        verify(loanRepository, never()).save(any());
    }
}
