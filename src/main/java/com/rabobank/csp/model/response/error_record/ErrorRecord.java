package com.rabobank.csp.model.response.error_record;

import lombok.Getter;

@Getter
public class ErrorRecord extends ErrorResponseRecord {
  private final int reference;

  public ErrorRecord(ErrorResponseRecordReference errorResponseRecordReference, String accountNumber) {
    super(accountNumber);
    this.reference = errorResponseRecordReference.getReference();
  }
}
