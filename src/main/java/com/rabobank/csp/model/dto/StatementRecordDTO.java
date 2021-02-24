package com.rabobank.csp.model.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class StatementRecordDTO {
  private Long transactionReference;
  private String accountNumber;
  private String startBalance;
  private BigDecimal mutation;
  private String description;
  private String endBalance;
}
