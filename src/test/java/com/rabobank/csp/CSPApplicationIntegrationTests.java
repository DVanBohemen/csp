package com.rabobank.csp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.rabobank.csp.controller.StatementRecordController;
import com.rabobank.csp.dao.StatementRecordRepository;
import com.rabobank.csp.model.StatementRecord;
import java.math.BigDecimal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CSPApplicationIntegrationTests {
	private final static String URL = "http://localhost:";
	private final static String POST_URI = "/statement-record";
	private static HttpHeaders headers;
	private static int testTransactionReference;

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private StatementRecordController statementRecordController;

	@Autowired
	private StatementRecordRepository statementRecordRepository;

	@BeforeAll
	public static void runBeforeAllTestMethods() {
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		testTransactionReference = 203;
	}

	@AfterEach
	public void runAfterEachTest() {
		statementRecordRepository.deleteAll();
	}

	@Test
	void contextLoads() {
		assertThat(statementRecordController, notNullValue());
	}

	@Test
	void post_With_Correct_Format_Returns_200_OK_And_Response_Body() {
		String responseBody = "{\"result\":\"SUCCESSFUL\",\"errorRecords\":[]}";
		String statementRecord = "{\"transactionReference\":" + testTransactionReference + ", \"accountNumber\": \"123\", \"startBalance\": \"€3.10\",\"mutation\": 2,\"description\": \"Joe\",\"endBalance\": \"5.10\"}";
		HttpEntity<String> request =
				new HttpEntity<String>(statementRecord, headers);

		ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(URL + port + POST_URI, request, String.class);
		assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
		assertThat(responseEntity.getBody(), is(responseBody));
	}

	@Test
	void post_With_Numeric_Format_Returns_200_OK_And_Response_Body() {
		String responseBody = "{\"result\":\"SUCCESSFUL\",\"errorRecords\":[]}";
		String statementRecord = "{\"transactionReference\":" + testTransactionReference + ", \"accountNumber\": \"123\", \"startBalance\": 3.10,\"mutation\": 2,\"description\": \"Joe\",\"endBalance\": 5.10}";
		HttpEntity<String> request =
				new HttpEntity<String>(statementRecord, headers);

		ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(URL + port + POST_URI, request, String.class);
		assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
		assertThat(responseEntity.getBody(), is(responseBody));
	}

	@Test
	void post_With_IncorrectEndBalance_Returns_200_INCORRECT_END_BALANCE_And_Correct_Error_Records() {
		String responseBody = "{\"result\":\"INCORRECT_END_BALANCE\",\"errorRecords\":[{\"reference\":1,\"accountNumber\":\"123\"}]}";
		String statementRecord = "{\"transactionReference\": 203, \"accountNumber\": \"123\", \"startBalance\": 3.10,\"mutation\": 2,\"description\": \"Joe\",\"endBalance\": 5.20}";
		HttpEntity<String> request =
				new HttpEntity<String>(statementRecord, headers);

		ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(URL + port + POST_URI, request, String.class);
		assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
		assertThat(responseEntity.getBody(), is(responseBody));
	}

	@Test
	void post_With_IncorrectEndBalance_Returns_200_INCORRECT_END_BALANCE_And_Error_Records() {
		String responseBody = "{\"result\":\"INCORRECT_END_BALANCE\",\"errorRecords\":[{\"reference\":1,\"accountNumber\":\"123\"}]}";
		String statementRecord = "{\"transactionReference\": 203, \"accountNumber\": \"123\", \"startBalance\": 3.10,\"mutation\": 2,\"description\": \"Joe\",\"endBalance\": 5.20}";
		HttpEntity<String> request =
				new HttpEntity<String>(statementRecord, headers);

		ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(URL + port + POST_URI, request, String.class);
		assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
		assertThat(responseEntity.getBody(), is(responseBody));
	}

	@Test
	void post_With_DuplicateRecord_Returns_200_DUPLICATE_REFERENCE_And_Error_Records() {
		StatementRecord statementTestRecord = new StatementRecord(203L, "TEST_ACCOUNT_1",
				BigDecimal.valueOf(10), BigDecimal.valueOf(10), "Test mutation",
				BigDecimal.valueOf(20));
		statementRecordRepository.save(statementTestRecord);

		String responseBody = "{\"result\":\"DUPLICATE_REFERENCE\",\"errorRecords\":[{\"reference\":1,\"accountNumber\":\"123\"}]}";
		String statementRecord = "{\"transactionReference\":" + testTransactionReference +", \"accountNumber\": \"123\", \"startBalance\": 3.10,\"mutation\": 2,\"description\": \"Joe\",\"endBalance\": 5.10}";
		HttpEntity<String> request =
				new HttpEntity<String>(statementRecord, headers);

		ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(URL + port + POST_URI, request, String.class);
		assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
		assertThat(responseEntity.getBody(), is(responseBody));
	}

	@Test
	void post_With_IncorrectEndBalanceAndDuplicateRecord_Returns_200_DUPLICATE_REFERENCE_INCORRECT_END_BALANCE_And_Error_Records() {
		StatementRecord statementTestRecord = new StatementRecord(203L, "TEST_ACCOUNT_1",
				BigDecimal.valueOf(10), BigDecimal.valueOf(10), "Test mutation",
				BigDecimal.valueOf(20));
		statementRecordRepository.save(statementTestRecord);

		String responseBody = "{\"result\":\"DUPLICATE_REFERENCE_INCORRECT_END_BALANCE\",\"errorRecords\":[{\"reference\":2,\"accountNumber\":\"123\"},"
				+ "{\"reference\":3,\"accountNumber\":\"123\"}]}";
		String statementRecord = "{\"transactionReference\":" + testTransactionReference +", \"accountNumber\": \"123\", \"startBalance\": 3.10,\"mutation\": 2,\"description\": \"Joe\",\"endBalance\": 5.20}";
		HttpEntity<String> request =
				new HttpEntity<String>(statementRecord, headers);

		ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(URL + port + POST_URI, request, String.class);
		assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
		assertThat(responseEntity.getBody(), is(responseBody));
	}

	@Test
	void post_With_Incorrect_Json_Returns_400_BAD_REQUEST_And_Response_Body() {
		String responseBody = "{\"result\":\"BAD_REQUEST\",\"errorRecords\":[]}";
		String statementRecord = "\"transactionReference\":" + testTransactionReference +", \"accountNumber\": \"123\", \"startBalance\": 3.10,\"mutation\": 2,\"description\": \"Joe\",\"endBalance\": 5.10}";
		HttpEntity<String> request =
				new HttpEntity<String>(statementRecord, headers);

		ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(URL + port + POST_URI, request, String.class);
		assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
		assertThat(responseEntity.getBody(), is(responseBody));
	}

	@Test
	void post_accountNumber_Null_Returns_500_INTERNAL_SERVER_ERROR_And_Response_Body() {
		String responseBody = "{\"result\":\"INTERNAL_SERVER_ERROR\",\"errorRecords\":[]}";
		String statementRecord = "{\"transactionReference\":" + testTransactionReference +", \"accountNumber\": null, \"startBalance\": 3.10,\"mutation\": 2,\"description\": \"Joe\",\"endBalance\": 5.10}";
		HttpEntity<String> request =
				new HttpEntity<String>(statementRecord, headers);

		ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(URL + port + POST_URI, request, String.class);
		assertThat(responseEntity.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
		assertThat(responseEntity.getBody(), is(responseBody));
	}
}
