package com.rabobank.csp.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.rabobank.csp.dao.StatementRecordRepository;
import com.rabobank.csp.model.StatementRecord;
import com.rabobank.csp.model.dto.ResponseMessageDTO;
import com.rabobank.csp.model.response.ResponseResult;
import com.rabobank.csp.model.response.error_record.DuplicateRecord;
import com.rabobank.csp.model.response.error_record.ErrorResponseRecordReference;
import com.rabobank.csp.model.response.error_record.IncorrectEndBalanceRecord;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StatementRecordProcessorServiceTests {
  private EndBalanceValidatorService endBalanceValidatorService;
  private StatementRecordRepository statementRecordRepository;

  private StatementRecordProcessor recordProcessorService;

  @BeforeEach
    void setUp() {
      endBalanceValidatorService = mock(EndBalanceValidatorService.class);
      statementRecordRepository = mock(StatementRecordRepository.class);

      recordProcessorService = new StatementRecordProcessorService(statementRecordRepository,
          endBalanceValidatorService);
    }

  @Test
  void processStatementRecord_Returns_Successful_When_Statement_Record_Could_Be_Successfully_Processed() {
    StatementRecord statementTestRecord = new StatementRecord(1L, "TEST_ACCOUNT_1",
        BigDecimal.valueOf(10), BigDecimal.valueOf(10), "Test mutation",
        BigDecimal.valueOf(20));

    ResponseMessageDTO responseMessageTestDTO = ResponseMessageDTO.builder()
        .result(ResponseResult.SUCCESSFUL)
        .errorRecords(Collections.emptyList())
        .build();

    when(endBalanceValidatorService.isValid(statementTestRecord)).thenReturn(true);

    ResponseMessageDTO responseMessageDTO = recordProcessorService.processStatementRecord(statementTestRecord);

    assertThat(responseMessageTestDTO.getResult(), is(responseMessageDTO.getResult()));
    assertThat(responseMessageTestDTO.getErrorRecords().size(), is(0));
  }

  @Test
  void processStatementRecord_Returns_INCORRECT_END_BALANCE_AND_DUPLICATE_REFERENCE_When_Statement_Record_Has_Incorrect_Balance_And_Id_Exists() {
    String testAccount = "TEST_ACCOUNT_1";

    StatementRecord statementTestRecord = new StatementRecord(1L, testAccount,
        BigDecimal.valueOf(10), BigDecimal.valueOf(10), "Test mutation",
        BigDecimal.valueOf(20));

    ResponseMessageDTO responseMessageTestDTO = ResponseMessageDTO.builder()
        .result(ResponseResult.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE)
        .errorRecords(Arrays.asList(new DuplicateRecord(ErrorResponseRecordReference.DUPLICATE, testAccount),
            new IncorrectEndBalanceRecord(ErrorResponseRecordReference.INCORRECT_END_BALANCE, testAccount)))
        .build();

    when(endBalanceValidatorService.isValid(statementTestRecord)).thenReturn(false);
    when(statementRecordRepository.existsById(statementTestRecord.getTransactionReference())).thenReturn(true);

    ResponseMessageDTO responseMessageDTO = recordProcessorService.processStatementRecord(statementTestRecord);
    assertThat(responseMessageTestDTO.getResult(), is(responseMessageDTO.getResult()));
    assertThat(responseMessageTestDTO.getErrorRecords().get(0).getAccountNumber(), is(responseMessageDTO.getErrorRecords().get(0)
        .getAccountNumber()));
    assertThat(responseMessageTestDTO.getErrorRecords().get(1).getAccountNumber(), is(responseMessageDTO.getErrorRecords().get(1)
        .getAccountNumber()));
    assertThat(responseMessageTestDTO.getErrorRecords().get(0).getClass(), is(responseMessageDTO.getErrorRecords().get(0).getClass()));
    assertThat(responseMessageTestDTO.getErrorRecords().get(1).getClass(), is(responseMessageDTO.getErrorRecords().get(1).getClass()));
  }

  @Test
  void processStatementRecord_Returns_INCORRECT_END_BALANCE_When_Statement_Record_Has_Incorrect_Balance() {
    String testAccount = "TEST_ACCOUNT_1";

    StatementRecord statementTestRecord = new StatementRecord(1L, testAccount,
        BigDecimal.valueOf(10), BigDecimal.valueOf(10), "Test mutation",
        BigDecimal.valueOf(20));

    ResponseMessageDTO responseMessageTestDTO = ResponseMessageDTO.builder()
        .result(ResponseResult.INCORRECT_END_BALANCE)
        .errorRecords(Collections.singletonList(new IncorrectEndBalanceRecord(ErrorResponseRecordReference.ERROR_RECORD,
            testAccount)))
        .build();

    when(endBalanceValidatorService.isValid(statementTestRecord)).thenReturn(false);
    when(statementRecordRepository.existsById(statementTestRecord.getTransactionReference())).thenReturn(false);

    ResponseMessageDTO responseMessageDTO = recordProcessorService.processStatementRecord(statementTestRecord);
    assertThat(responseMessageTestDTO.getResult(), is(responseMessageDTO.getResult()));
    assertThat(responseMessageTestDTO.getErrorRecords().get(0).getAccountNumber(), is(responseMessageDTO.getErrorRecords().get(0)
    .getAccountNumber()));
    assertThat(responseMessageTestDTO.getErrorRecords().get(0).getClass(), is(responseMessageDTO.getErrorRecords().get(0).getClass()));
  }

  @Test
  void processStatementRecord_Returns_DUPLICATE_RECORD_When_Id_Exists() {
    String testAccount = "TEST_ACCOUNT_1";

    StatementRecord statementTestRecord = new StatementRecord(1L, testAccount,
        BigDecimal.valueOf(10), BigDecimal.valueOf(10), "Test mutation",
        BigDecimal.valueOf(20));

    ResponseMessageDTO responseMessageTestDTO = ResponseMessageDTO.builder()
        .result(ResponseResult.DUPLICATE_REFERENCE)
        .errorRecords(Collections.singletonList(new DuplicateRecord(ErrorResponseRecordReference.ERROR_RECORD,
            testAccount)))
        .build();

    when(endBalanceValidatorService.isValid(statementTestRecord)).thenReturn(true);
    when(statementRecordRepository.existsById(statementTestRecord.getTransactionReference())).thenReturn(true);

    ResponseMessageDTO responseMessageDTO = recordProcessorService.processStatementRecord(statementTestRecord);
    assertThat(responseMessageTestDTO.getResult(), is(responseMessageDTO.getResult()));
    assertThat(responseMessageTestDTO.getErrorRecords().get(0).getAccountNumber(), is(responseMessageDTO.getErrorRecords().get(0)
        .getAccountNumber()));
    assertThat(responseMessageTestDTO.getErrorRecords().get(0).getClass(), is(responseMessageDTO.getErrorRecords().get(0).getClass()));
  }
}
