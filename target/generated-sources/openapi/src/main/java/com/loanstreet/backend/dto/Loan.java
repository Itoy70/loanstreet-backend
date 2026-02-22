package com.loanstreet.backend.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.util.UUID;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Complete loan entity including generated ID
 */

@Schema(name = "Loan", description = "Complete loan entity including generated ID")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-21T18:28:57.351874-06:00[America/Chicago]", comments = "Generator version: 7.4.0")
public class Loan {

  private UUID id;

  private BigDecimal amount;

  private BigDecimal interestRate;

  private Integer lengthInMonths;

  private BigDecimal monthlyPaymentAmount;

  public Loan() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Loan(UUID id, BigDecimal amount, BigDecimal interestRate, Integer lengthInMonths, BigDecimal monthlyPaymentAmount) {
    this.id = id;
    this.amount = amount;
    this.interestRate = interestRate;
    this.lengthInMonths = lengthInMonths;
    this.monthlyPaymentAmount = monthlyPaymentAmount;
  }

  public Loan id(UUID id) {
    this.id = id;
    return this;
  }

  /**
   * Auto-generated UUID for the loan
   * @return id
  */
  @NotNull @Valid 
  @Schema(name = "id", example = "550e8400-e29b-41d4-a716-446655440000", description = "Auto-generated UUID for the loan", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Loan amount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Loan amount
   * @return amount
  */
  @NotNull @Valid 
  @Schema(name = "amount", example = "10000.0", description = "Loan amount", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("amount")
  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public Loan interestRate(BigDecimal interestRate) {
    this.interestRate = interestRate;
    return this;
  }

  /**
   * Annual interest rate as a decimal
   * @return interestRate
  */
  @NotNull @Valid 
  @Schema(name = "interestRate", example = "0.05", description = "Annual interest rate as a decimal", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("interestRate")
  public BigDecimal getInterestRate() {
    return interestRate;
  }

  public void setInterestRate(BigDecimal interestRate) {
    this.interestRate = interestRate;
  }

  public Loan lengthInMonths(Integer lengthInMonths) {
    this.lengthInMonths = lengthInMonths;
    return this;
  }

  /**
   * Loan term in months
   * @return lengthInMonths
  */
  @NotNull 
  @Schema(name = "lengthInMonths", example = "36", description = "Loan term in months", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("lengthInMonths")
  public Integer getLengthInMonths() {
    return lengthInMonths;
  }

  public void setLengthInMonths(Integer lengthInMonths) {
    this.lengthInMonths = lengthInMonths;
  }

  public Loan monthlyPaymentAmount(BigDecimal monthlyPaymentAmount) {
    this.monthlyPaymentAmount = monthlyPaymentAmount;
    return this;
  }

  /**
   * Calculated monthly payment amount
   * @return monthlyPaymentAmount
  */
  @NotNull @Valid 
  @Schema(name = "monthlyPaymentAmount", example = "299.71", description = "Calculated monthly payment amount", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("monthlyPaymentAmount")
  public BigDecimal getMonthlyPaymentAmount() {
    return monthlyPaymentAmount;
  }

  public void setMonthlyPaymentAmount(BigDecimal monthlyPaymentAmount) {
    this.monthlyPaymentAmount = monthlyPaymentAmount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Loan loan = (Loan) o;
    return Objects.equals(this.id, loan.id) &&
        Objects.equals(this.amount, loan.amount) &&
        Objects.equals(this.interestRate, loan.interestRate) &&
        Objects.equals(this.lengthInMonths, loan.lengthInMonths) &&
        Objects.equals(this.monthlyPaymentAmount, loan.monthlyPaymentAmount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, amount, interestRate, lengthInMonths, monthlyPaymentAmount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Loan {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    interestRate: ").append(toIndentedString(interestRate)).append("\n");
    sb.append("    lengthInMonths: ").append(toIndentedString(lengthInMonths)).append("\n");
    sb.append("    monthlyPaymentAmount: ").append(toIndentedString(monthlyPaymentAmount)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

