package com.rabobank.csp.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabobank.csp.model.StatementRecord;
import com.rabobank.csp.model.dto.ResponseMessageDTO;
import com.rabobank.csp.model.dto.StatementRecordDTO;
import com.rabobank.csp.model.response.ResponseResult;
import com.rabobank.csp.service.StatementRecordDTOMapper;
import com.rabobank.csp.service.StatementRecordDTOMapperService;
import com.rabobank.csp.service.StatementRecordProcessor;
import com.rabobank.csp.service.StatementRecordProcessorService;
import java.math.BigDecimal;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class StatementRecordControllerTests {
  private StatementRecordProcessor statementRecordProcessorService;
  private StatementRecordDTOMapper statementRecordDTOMapper;

  private final static String URI = "/statement-record";

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    statementRecordProcessorService = mock(StatementRecordProcessorService.class);
    statementRecordDTOMapper = mock(StatementRecordDTOMapperService.class);

    StatementRecordController statementRecordController = new StatementRecordController(statementRecordProcessorService,
        statementRecordDTOMapper);
    mockMvc = MockMvcBuilders.standaloneSetup(statementRecordController).build();
  }

  @Test
  void postStatementRecord_Should_Return_400_BAD_REQUEST_Error_When_Body_Is_Empty() throws Exception {
    mockMvc.perform(post(URI)).andExpect(status().isBadRequest());
  }

  @Test
  void postStatementRecord_Should_Return_200_OK_And_Response_Message_When_Record_Could_Be_Processed() throws Exception {
    StatementRecord statementTestRecord = new StatementRecord(1L, "TEST_ACCOUNT_1",
        BigDecimal.valueOf(10), BigDecimal.valueOf(10), "Test mutation",
        BigDecimal.valueOf(20));
    ResponseMessageDTO responseMessageTestDTO = ResponseMessageDTO.builder()
        .result(ResponseResult.SUCCESSFUL)
        .errorRecords(Collections.emptyList())
        .build();
    StatementRecordDTO statementTestRecordDTO = new StatementRecordDTO(1L, "TEST_ACCOUNT_1",
        "10", BigDecimal.valueOf(10), "Test mutation",
        "20");

    when(statementRecordDTOMapper.mapDTOToStatementRecord(statementTestRecordDTO)).thenReturn(statementTestRecord);
    when(statementRecordProcessorService.processStatementRecord(statementTestRecord)).thenReturn(responseMessageTestDTO);

    mockMvc.perform(post(URI)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(new ObjectMapper().writeValueAsString(statementTestRecordDTO)))
        .andExpect(status().is2xxSuccessful());
  }

  @Test
  void postStatementRecord_Should_Return_400_BAD_REQUEST_When_Json_Could_Not_Be_Parsed() throws Exception {
    StatementRecord statementTestRecord = new StatementRecord(1L, "TEST_ACCOUNT_1",
        BigDecimal.valueOf(10), BigDecimal.valueOf(10), "Test mutation",
        BigDecimal.valueOf(20));
    StatementRecordDTO statementTestRecordDTO = new StatementRecordDTO(1L, "TEST_ACCOUNT_1",
        "10", BigDecimal.valueOf(10), "Test mutation",
        "20");

    when(statementRecordDTOMapper.mapDTOToStatementRecord(statementTestRecordDTO)).thenReturn(statementTestRecord);

    mockMvc.perform(post(URI)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(statementTestRecord.toString()))
        .andExpect(status().isBadRequest());
  }

  @Test
  void postStatementRecord_Should_Return_400_BAD_REQUEST_When_Json_Missing_Curly_Bracket() throws Exception {
    String json = "{ \"transactionReference\" : 2, \"accountNumber\" : \"NL-INGB\", \"startBalance\" : 3.50, \"mutation\" : 2.10,"
        + "\"description\" : \"Just a description\", \"endBalance\" : 5.10 ";
    mockMvc.perform(post(URI)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(json))
        .andExpect(status().isBadRequest());
  }

  @Test
  void postStatementRecord_Should_Return_400_BAD_REQUEST_When_Json_Missing_Double_Quotation_Marks() throws Exception {
    String json = "{ \"transactionReference\" : 2, \"accountNumber : \"NL-INGB\", \"startBalance\" : 3.50, \"mutation\" : 2.10,"
        + "\"description\" : \"Just a description\", \"endBalance\" : 5.10 }";
    mockMvc.perform(post(URI)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(json))
        .andExpect(status().isBadRequest());
  }
}
