package com.rabobank.csp.model.response.error_record;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public enum ErrorResponseRecordReference {
  ERROR_RECORD(1),
  DUPLICATE(2),
  INCORRECT_END_BALANCE(3);

  private final Integer reference;

  ErrorResponseRecordReference(Integer reference) {
    this.reference = reference;
  }

}
