package com.rabobank.csp.controller;

import com.rabobank.csp.model.StatementRecord;
import com.rabobank.csp.model.dto.ResponseMessageDTO;
import com.rabobank.csp.model.dto.StatementRecordDTO;
import com.rabobank.csp.service.StatementRecordDTOMapper;
import com.rabobank.csp.service.StatementRecordProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatementRecordController {

    private final StatementRecordProcessor statementRecordProcessorService;
    private final StatementRecordDTOMapper statementRecordDTOMapper;

    @Autowired
    public StatementRecordController(StatementRecordProcessor statementRecordProcessorService,
                                     StatementRecordDTOMapper statementRecordDTOMapper) {
        this.statementRecordProcessorService = statementRecordProcessorService;
        this.statementRecordDTOMapper = statementRecordDTOMapper;
    }

    @PostMapping("/statement-record")
    public ResponseMessageDTO postStatementRecord(@RequestBody StatementRecordDTO statementRecordDTO) {
        StatementRecord statementRecord = statementRecordDTOMapper.mapDTOToStatementRecord(statementRecordDTO);
        return statementRecordProcessorService.processStatementRecord(statementRecord);
    }
}
