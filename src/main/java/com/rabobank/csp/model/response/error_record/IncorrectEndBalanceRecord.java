package com.rabobank.csp.model.response.error_record;

import lombok.Getter;

@Getter
public class IncorrectEndBalanceRecord extends ErrorResponseRecord {

  private int reference;

  public IncorrectEndBalanceRecord(ErrorResponseRecordReference errorResponseRecordReference,
                                   String accountNumber) {
    super(accountNumber);
    this.reference = errorResponseRecordReference.getReference();
  }
}
