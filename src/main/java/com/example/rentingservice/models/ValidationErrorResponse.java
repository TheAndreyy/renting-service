package com.example.rentingservice.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ValidationErrorResponse(
        String message,
        String errorCode,
        String field
) {
    public ValidationErrorResponse(String message, String errorCode) {
        this(message, errorCode, null);
    }
}
