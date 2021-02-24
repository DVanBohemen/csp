package com.rabobank.csp.model.response.error_record;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"reference", "accountNumber"})
public class ErrorResponseRecord {
  private String accountNumber;

  public ErrorResponseRecord(String accountNumber) {
    this.accountNumber = accountNumber;
  }
}
