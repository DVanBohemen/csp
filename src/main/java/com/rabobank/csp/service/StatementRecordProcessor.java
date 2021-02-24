package com.rabobank.csp.service;

import com.rabobank.csp.model.StatementRecord;
import com.rabobank.csp.model.dto.ResponseMessageDTO;

public interface StatementRecordProcessor {
  ResponseMessageDTO processStatementRecord(StatementRecord statementRecord);
}
