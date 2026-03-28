package com.pharmaTrace.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private Boolean success = false;

    private String message;

    private Integer statusCode;

    private LocalDateTime timestamp = LocalDateTime.now();

    private String path;

    private Map<String, String> fieldErrors;

    public static ErrorResponse of(String message, Integer statusCode) {
        return ErrorResponse.builder()
                .message(message)
                .statusCode(statusCode)
                .build();
    }
}