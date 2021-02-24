package com.rabobank.csp.service;

import com.rabobank.csp.dao.StatementRecordRepository;
import com.rabobank.csp.model.StatementRecord;
import com.rabobank.csp.model.dto.ResponseMessageDTO;
import com.rabobank.csp.model.response.ResponseResult;
import com.rabobank.csp.model.response.error_record.DuplicateRecord;
import com.rabobank.csp.model.response.error_record.ErrorResponseRecord;
import com.rabobank.csp.model.response.error_record.ErrorResponseRecordReference;
import com.rabobank.csp.model.response.error_record.IncorrectEndBalanceRecord;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatementRecordProcessorService implements StatementRecordProcessor {
    private final StatementRecordRepository statementRecordRepository;
    private final EndBalanceValidatorService endBalanceValidatorService;

    @Autowired
    public StatementRecordProcessorService(StatementRecordRepository statementRecordRepository,
                                           EndBalanceValidatorService endBalanceValidatorService) {
        this.statementRecordRepository = statementRecordRepository;
        this.endBalanceValidatorService = endBalanceValidatorService;
    }

    public ResponseMessageDTO processStatementRecord(StatementRecord statementRecord) {
        if (endBalanceIsValidAndIdDoesntExist(statementRecord)) {
            statementRecordRepository.save(statementRecord);
            return mapResponseMessage(ResponseResult.SUCCESSFUL, Collections.emptyList());
        } else if (endBalanceIsNotValidAndIdExists(statementRecord)) {
            return mapResponseMessage(ResponseResult.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE, Arrays.asList(
                new DuplicateRecord(ErrorResponseRecordReference.DUPLICATE, statementRecord.getAccountNumber()),
                new IncorrectEndBalanceRecord(ErrorResponseRecordReference.INCORRECT_END_BALANCE, statementRecord.getAccountNumber())));
        } else if (!endBalanceIsValid(statementRecord)) {
            return mapResponseMessage(ResponseResult.INCORRECT_END_BALANCE, Collections.singletonList(
                new IncorrectEndBalanceRecord(ErrorResponseRecordReference.ERROR_RECORD, statementRecord.getAccountNumber())));
        } else {
            return mapResponseMessage(ResponseResult.DUPLICATE_REFERENCE, Collections.singletonList(
                new DuplicateRecord(ErrorResponseRecordReference.ERROR_RECORD, statementRecord.getAccountNumber())));
        }
    }

    private boolean endBalanceIsValid(StatementRecord statementRecord) {
        return endBalanceValidatorService.isValid(statementRecord);
    }

    private boolean endBalanceIsValidAndIdDoesntExist(StatementRecord statementRecord) {
        return endBalanceIsValid(statementRecord) &&
            !statementRecordRepository.existsById(statementRecord.getTransactionReference());
    }

    private boolean endBalanceIsNotValidAndIdExists(StatementRecord statementRecord) {
        return !endBalanceIsValid(statementRecord) &&
            statementRecordRepository.existsById(statementRecord.getTransactionReference());
    }

    private ResponseMessageDTO mapResponseMessage(ResponseResult responseResult, List<ErrorResponseRecord> errorRecords) {
        return ResponseMessageDTO.builder().result(responseResult).errorRecords(errorRecords).build();
    }
}
