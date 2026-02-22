package com.loanstreet.backend.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Request payload for creating a new loan (ID is auto-generated)
 */

@Schema(name = "LoanCreateRequest", description = "Request payload for creating a new loan (ID is auto-generated)")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-21T18:28:57.351874-06:00[America/Chicago]", comments = "Generator version: 7.4.0")
public class LoanCreateRequest {

  private BigDecimal amount;

  private BigDecimal interestRate;

  private Integer lengthInMonths;

  private BigDecimal monthlyPaymentAmount;

  public LoanCreateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LoanCreateRequest(BigDecimal amount, BigDecimal interestRate, Integer lengthInMonths, BigDecimal monthlyPaymentAmount) {
    this.amount = amount;
    this.interestRate = interestRate;
    this.lengthInMonths = lengthInMonths;
    this.monthlyPaymentAmount = monthlyPaymentAmount;
  }

  public LoanCreateRequest amount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Loan amount
   * minimum: 0
   * @return amount
  */
  @NotNull @Valid @DecimalMin(value = "0", inclusive = false) 
  @Schema(name = "amount", example = "10000.0", description = "Loan amount", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("amount")
  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public LoanCreateRequest interestRate(BigDecimal interestRate) {
    this.interestRate = interestRate;
    return this;
  }

  /**
   * Annual interest rate as a decimal (0 to 1)
   * minimum: 0
   * maximum: 1.0
   * @return interestRate
  */
  @NotNull @Valid @DecimalMin(value = "0", inclusive = false) @DecimalMax("1.0") 
  @Schema(name = "interestRate", example = "0.05", description = "Annual interest rate as a decimal (0 to 1)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("interestRate")
  public BigDecimal getInterestRate() {
    return interestRate;
  }

  public void setInterestRate(BigDecimal interestRate) {
    this.interestRate = interestRate;
  }

  public LoanCreateRequest lengthInMonths(Integer lengthInMonths) {
    this.lengthInMonths = lengthInMonths;
    return this;
  }

  /**
   * Loan term in months
   * minimum: 1
   * maximum: 600
   * @return lengthInMonths
  */
  @NotNull @Min(1) @Max(600) 
  @Schema(name = "lengthInMonths", example = "36", description = "Loan term in months", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("lengthInMonths")
  public Integer getLengthInMonths() {
    return lengthInMonths;
  }

  public void setLengthInMonths(Integer lengthInMonths) {
    this.lengthInMonths = lengthInMonths;
  }

  public LoanCreateRequest monthlyPaymentAmount(BigDecimal monthlyPaymentAmount) {
    this.monthlyPaymentAmount = monthlyPaymentAmount;
    return this;
  }

  /**
   * Calculated monthly payment amount
   * minimum: 0
   * @return monthlyPaymentAmount
  */
  @NotNull @Valid @DecimalMin("0") 
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
    LoanCreateRequest loanCreateRequest = (LoanCreateRequest) o;
    return Objects.equals(this.amount, loanCreateRequest.amount) &&
        Objects.equals(this.interestRate, loanCreateRequest.interestRate) &&
        Objects.equals(this.lengthInMonths, loanCreateRequest.lengthInMonths) &&
        Objects.equals(this.monthlyPaymentAmount, loanCreateRequest.monthlyPaymentAmount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount, interestRate, lengthInMonths, monthlyPaymentAmount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoanCreateRequest {\n");
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

