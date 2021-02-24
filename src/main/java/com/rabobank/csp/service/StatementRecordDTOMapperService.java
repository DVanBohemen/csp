package com.rabobank.csp.service;

import com.rabobank.csp.model.StatementRecord;
import com.rabobank.csp.model.dto.StatementRecordDTO;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class StatementRecordDTOMapperService implements StatementRecordDTOMapper {

  @Override
  public StatementRecord mapDTOToStatementRecord(StatementRecordDTO statementRecordDTO) {
      String startBalance = statementRecordDTO.getStartBalance().replace("€", "").trim();
      String endBalance = statementRecordDTO.getEndBalance().replace("€", "").trim();

      return StatementRecord.builder()
          .transactionReference(statementRecordDTO.getTransactionReference())
          .accountNumber(statementRecordDTO.getAccountNumber())
          .startBalance(new BigDecimal(startBalance))
          .mutation(statementRecordDTO.getMutation())
          .description(statementRecordDTO.getDescription())
          .endBalance(new BigDecimal(endBalance))
          .build();
  }
}
