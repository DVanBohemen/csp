package com.rabobank.csp.service;

import com.rabobank.csp.model.StatementRecord;
import com.rabobank.csp.model.dto.StatementRecordDTO;

public interface StatementRecordDTOMapper {
StatementRecord mapDTOToStatementRecord(StatementRecordDTO statementRecordDTO);
}
