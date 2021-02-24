package com.rabobank.csp.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.rabobank.csp.model.StatementRecord;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EndBalanceValidatorServiceTests {
  EndBalanceValidatorService endBalanceValidatorService;

  @BeforeEach
  void setUp() {
    endBalanceValidatorService = new EndBalanceValidatorService();
  }

  @Test
  void endBalanceIsValid_When_Mutation_Of_10_Matches_EndResult() {
    StatementRecord statementRecord = new StatementRecord(1L, "TEST_ACCOUNT_1", BigDecimal.valueOf(10), BigDecimal.valueOf(10), "Test mutation",
        BigDecimal.valueOf(20));

    boolean endBalanceIsValid = endBalanceValidatorService.isValid(statementRecord);

    assertThat(endBalanceIsValid, is(true));
  }

  @Test
  void endBalanceIsValid_When_Mutation_Of_Minus_10_Matches_EndResult() {
    StatementRecord statementRecord = new StatementRecord(1L, "TEST_ACCOUNT_1", BigDecimal.valueOf(10), BigDecimal.valueOf(-10), "Test mutation",
        BigDecimal.valueOf(0));

    boolean endBalanceIsValid = endBalanceValidatorService.isValid(statementRecord);

    assertThat(endBalanceIsValid, is(true));
  }

  @Test
  void endBalanceIsValid_When_Mutation_Of_0_Matches_EndResult() {
    StatementRecord statementRecord = new StatementRecord(1L, "TEST_ACCOUNT_1", BigDecimal.valueOf(0), BigDecimal.valueOf(0), "Test mutation",
        BigDecimal.valueOf(0));

    boolean endBalanceIsValid = endBalanceValidatorService.isValid(statementRecord);

    assertThat(endBalanceIsValid, is(true));
  }

  @Test
  void endBalanceIsValid_Is_False_When_Mutation_Of_Minus_10_Not_Matches_EndResult() {
    StatementRecord statementRecord = new StatementRecord(1L, "TEST_ACCOUNT_1", BigDecimal.valueOf(10), BigDecimal.valueOf(-10), "Test mutation",
        BigDecimal.valueOf(10));

    boolean endBalanceIsValid = endBalanceValidatorService.isValid(statementRecord);

    assertThat(endBalanceIsValid, is(false));
  }

  @Test
  void endBalanceIsValid_Is_False_When_Mutation_Of_Minus_10_Not_Matches_Decimal_EndResult() {
    StatementRecord statementRecord = new StatementRecord(1L, "TEST_ACCOUNT_1", BigDecimal.valueOf(10), BigDecimal.valueOf(-10), "Test mutation",
        BigDecimal.valueOf(0.01));

    boolean endBalanceIsValid = endBalanceValidatorService.isValid(statementRecord);

    assertThat(endBalanceIsValid, is(false));
  }

}
