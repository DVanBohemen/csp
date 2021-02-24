package com.rabobank.csp.dao;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.rabobank.csp.model.StatementRecord;
import java.math.BigDecimal;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class StatementRecordRepositoryIntegrationTests {
  @Autowired
  private StatementRecordRepository statementRecordRepository;

  @BeforeEach
  void setUp() {
    statementRecordRepository.deleteAll();
  }

  @Test
  void statementRecordRepository_Is_Initialized() {
    assertThat(statementRecordRepository, notNullValue());
  }

  @Test
  void existsById_Returns_False_If_Id_Doesnt_Exists() {
    assertThat(statementRecordRepository.existsById(1L), CoreMatchers.is(false));
  }

  @Test
  void existsById_Returns_True_If_Id_Exists() {
    StatementRecord statementTestRecord = new StatementRecord(1L, "TEST_ACCOUNT_1",
        BigDecimal.valueOf(10), BigDecimal.valueOf(10), "Test mutation",
        BigDecimal.valueOf(20));
    statementRecordRepository.save(statementTestRecord);
    assertThat(statementRecordRepository.existsById(1L), CoreMatchers.is(true));
  }

  @Test
  void save_Throws_Exception_If_AccountNumber_Is_Greater_Than_34_Chars() {
    StatementRecord statementTestRecord = new StatementRecord(1L,
        "TEST_ACCOUNT_1_IS_A_WAY_TO_LONG_ONE",
        BigDecimal.valueOf(10), BigDecimal.valueOf(10), "Test mutation",
        BigDecimal.valueOf(20));

    Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
      statementRecordRepository.save(statementTestRecord);
    });
  }
}
