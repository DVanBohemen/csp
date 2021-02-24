package com.rabobank.csp.service;

import com.rabobank.csp.model.StatementRecord;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class EndBalanceValidatorService {
public boolean isValid(StatementRecord statementRecord) {
  BigDecimal endBalanceToValidate = statementRecord.getEndBalance();
  BigDecimal calculatedEndBalance = statementRecord.getStartBalance().add(statementRecord.getMutation());
  return calculatedEndBalance.equals(endBalanceToValidate);
  }
}
