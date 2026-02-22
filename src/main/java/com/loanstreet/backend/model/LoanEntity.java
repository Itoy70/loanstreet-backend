package com.loanstreet.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "loans")
@Getter
@Setter
@NoArgsConstructor
public class LoanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(name = "interest_rate", nullable = false, precision = 10, scale = 6)
    private BigDecimal interestRate;

    @Column(name = "length_in_months", nullable = false)
    private Integer lengthInMonths;

    @Column(name = "monthly_payment_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal monthlyPaymentAmount;

    /** 
     * Consider adding updated time and created time fields to the entity
     * Consider also adding created by and updated by fields to the entity
     * Consider also adding deleted flag to the entity
     *  
     * */ 
}
