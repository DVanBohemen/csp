package com.rabobank.csp.controller;

import com.rabobank.csp.model.dto.ResponseMessageDTO;
import com.rabobank.csp.model.response.ResponseResult;
import java.util.Collections;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

  @org.springframework.web.bind.annotation.ExceptionHandler
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ResponseMessageDTO mapBadRequestException(HttpMessageNotReadableException e) {
  return ResponseMessageDTO.builder()
      .result(ResponseResult.BAD_REQUEST)
      .errorRecords(Collections.emptyList())
      .build();
  }

  @org.springframework.web.bind.annotation.ExceptionHandler
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public ResponseMessageDTO mapInternalServerErrorException(Exception e) {
    return ResponseMessageDTO.builder()
        .result(ResponseResult.INTERNAL_SERVER_ERROR)
        .errorRecords(Collections.emptyList())
        .build();
  }
}
