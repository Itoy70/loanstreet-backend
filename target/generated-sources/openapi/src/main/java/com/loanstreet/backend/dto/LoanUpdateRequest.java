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
 * Request payload for a full replacement update of an existing loan. All fields are required.
 */

@Schema(name = "LoanUpdateRequest", description = "Request payload for a full replacement update of an existing loan. All fields are required.")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-21T18:28:57.351874-06:00[America/Chicago]", comments = "Generator version: 7.4.0")
public class LoanUpdateRequest {

  private BigDecimal amount;

  private BigDecimal interestRate;

  private Integer lengthInMonths;

  private BigDecimal monthlyPaymentAmount;

  public LoanUpdateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LoanUpdateRequest(BigDecimal amount, BigDecimal interestRate, Integer lengthInMonths, BigDecimal monthlyPaymentAmount) {
    this.amount = amount;
    this.interestRate = interestRate;
    this.lengthInMonths = lengthInMonths;
    this.monthlyPaymentAmount = monthlyPaymentAmount;
  }

  public LoanUpdateRequest amount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Loan amount
   * minimum: 0
   * @return amount
  */
  @NotNull @Valid @DecimalMin(value = "0", inclusive = false) 
  @Schema(name = "amount", example = "15000.0", description = "Loan amount", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("amount")
  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public LoanUpdateRequest interestRate(BigDecimal interestRate) {
    this.interestRate = interestRate;
    return this;
  }

  /**
   * Annual interest rate as a decimal (0 to 1)
   * minimum: 0
   * maximum: 1.0
   * @return interestRate
  */
  @NotNull @Valid @DecimalMin("0") @DecimalMax("1.0") 
  @Schema(name = "interestRate", example = "0.045", description = "Annual interest rate as a decimal (0 to 1)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("interestRate")
  public BigDecimal getInterestRate() {
    return interestRate;
  }

  public void setInterestRate(BigDecimal interestRate) {
    this.interestRate = interestRate;
  }

  public LoanUpdateRequest lengthInMonths(Integer lengthInMonths) {
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
  @Schema(name = "lengthInMonths", example = "48", description = "Loan term in months", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("lengthInMonths")
  public Integer getLengthInMonths() {
    return lengthInMonths;
  }

  public void setLengthInMonths(Integer lengthInMonths) {
    this.lengthInMonths = lengthInMonths;
  }

  public LoanUpdateRequest monthlyPaymentAmount(BigDecimal monthlyPaymentAmount) {
    this.monthlyPaymentAmount = monthlyPaymentAmount;
    return this;
  }

  /**
   * Calculated monthly payment amount
   * minimum: 0
   * @return monthlyPaymentAmount
  */
  @NotNull @Valid @DecimalMin(value = "0", inclusive = false) 
  @Schema(name = "monthlyPaymentAmount", example = "342.05", description = "Calculated monthly payment amount", requiredMode = Schema.RequiredMode.REQUIRED)
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
    LoanUpdateRequest loanUpdateRequest = (LoanUpdateRequest) o;
    return Objects.equals(this.amount, loanUpdateRequest.amount) &&
        Objects.equals(this.interestRate, loanUpdateRequest.interestRate) &&
        Objects.equals(this.lengthInMonths, loanUpdateRequest.lengthInMonths) &&
        Objects.equals(this.monthlyPaymentAmount, loanUpdateRequest.monthlyPaymentAmount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount, interestRate, lengthInMonths, monthlyPaymentAmount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoanUpdateRequest {\n");
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

