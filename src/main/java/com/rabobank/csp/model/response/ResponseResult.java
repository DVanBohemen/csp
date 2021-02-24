package com.rabobank.csp.model.response;

import lombok.ToString;

@ToString
public enum ResponseResult {
  SUCCESSFUL("SUCCESSFUL"),
  DUPLICATE_REFERENCE("DUPLICATE_REFERENCE"),
  INCORRECT_END_BALANCE("INCORRECT_END_BALANCE"),
  DUPLICATE_REFERENCE_INCORRECT_END_BALANCE("DUPLICATE_REFERENCE_INCORRECT_END_BALANCE"),
  BAD_REQUEST("BAD_REQUEST"),
  INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR");

  private final String responseResult;

  ResponseResult(String responseResult) {
    this.responseResult = responseResult;
  }
}
