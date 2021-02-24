package com.rabobank.csp.model.dto;

import com.rabobank.csp.model.response.ResponseResult;
import com.rabobank.csp.model.response.error_record.ErrorResponseRecord;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResponseMessageDTO {
    private ResponseResult result;
    private List<ErrorResponseRecord> errorRecords;
}
