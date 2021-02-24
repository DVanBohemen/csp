package com.rabobank.csp.model.response.error_record;

import lombok.Getter;

@Getter
public class DuplicateRecord extends ErrorResponseRecord {
  private final int reference;

  public DuplicateRecord(ErrorResponseRecordReference errorResponseRecordReference, String accountNumber) {
    super(accountNumber);
    this.reference = errorResponseRecordReference.getReference();
  }
}
