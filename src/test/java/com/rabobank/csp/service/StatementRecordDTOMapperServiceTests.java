package com.rabobank.csp.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.rabobank.csp.model.StatementRecord;
import com.rabobank.csp.model.dto.StatementRecordDTO;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StatementRecordDTOMapperServiceTests {
  private StatementRecordDTOMapper statementRecordDTOMapper;

  @BeforeEach
  void setUp() {
    statementRecordDTOMapper = new StatementRecordDTOMapperService();
  }

  @Test
  void mapDTOToStatementRecord_Should_Remove_Euro_Sign_And_Map_DTO_To_Statement_Record() {
    StatementRecord statementTestRecord = new StatementRecord(1L, "TEST_ACCOUNT_1",
        BigDecimal.valueOf(10), BigDecimal.valueOf(10), "Test mutation",
        BigDecimal.valueOf(20));
    StatementRecordDTO statementTestRecordDTO = new StatementRecordDTO(1L, "TEST_ACCOUNT_1",
        "€10", BigDecimal.valueOf(10), "Test mutation",
        "€20");
    StatementRecord statementRecord = statementRecordDTOMapper.mapDTOToStatementRecord(statementTestRecordDTO);

    assertThat(statementRecord.getMutation(), is(statementTestRecord.getMutation()));
    assertThat(statementRecord.getEndBalance(), is(statementTestRecord.getEndBalance()));
    assertThat(statementRecord.getAccountNumber(), is(statementTestRecord.getAccountNumber()));
    assertThat(statementRecord.getDescription(), is(statementTestRecord.getDescription()));
    assertThat(statementRecord.getTransactionReference(), is(statementTestRecord.getTransactionReference()));
  }

  @Test
  void mapDTOToStatementRecord_Should_Remove_Euro_Sign_And_Remove_Spaces_And_Map_DTO_To_Statement_Record() {
    StatementRecord statementTestRecord = new StatementRecord(1L, "TEST_ACCOUNT_1",
        BigDecimal.valueOf(10), BigDecimal.valueOf(10), "Test mutation",
        BigDecimal.valueOf(20));
    StatementRecordDTO statementTestRecordDTO = new StatementRecordDTO(1L, "TEST_ACCOUNT_1",
        "€ 10 ", BigDecimal.valueOf(10), "Test mutation",
        "€ 20 ");
    StatementRecord statementRecord = statementRecordDTOMapper.mapDTOToStatementRecord(statementTestRecordDTO);


    assertThat(statementRecord.getMutation(), is(statementTestRecord.getMutation()));
    assertThat(statementRecord.getEndBalance(), is(statementTestRecord.getEndBalance()));
    assertThat(statementRecord.getAccountNumber(), is(statementTestRecord.getAccountNumber()));
    assertThat(statementRecord.getDescription(), is(statementTestRecord.getDescription()));
    assertThat(statementRecord.getTransactionReference(), is(statementTestRecord.getTransactionReference()));
  }

  @Test
  void mapDTOToStatementRecord_Without_Euro_Sign_Should_Map_DTO_To_Statement_Record() {
    StatementRecord statementTestRecord = new StatementRecord(1L, "TEST_ACCOUNT_1",
        BigDecimal.valueOf(10), BigDecimal.valueOf(10), "Test mutation",
        BigDecimal.valueOf(20));
    StatementRecordDTO statementTestRecordDTO = new StatementRecordDTO(1L, "TEST_ACCOUNT_1",
        "10", BigDecimal.valueOf(10), "Test mutation",
        "20");
    StatementRecord statementRecord = statementRecordDTOMapper.mapDTOToStatementRecord(statementTestRecordDTO);

    assertThat(statementRecord.getMutation(), is(statementTestRecord.getMutation()));
    assertThat(statementRecord.getEndBalance(), is(statementTestRecord.getEndBalance()));
    assertThat(statementRecord.getAccountNumber(), is(statementTestRecord.getAccountNumber()));
    assertThat(statementRecord.getDescription(), is(statementTestRecord.getDescription()));
    assertThat(statementRecord.getTransactionReference(), is(statementTestRecord.getTransactionReference()));
  }
}
