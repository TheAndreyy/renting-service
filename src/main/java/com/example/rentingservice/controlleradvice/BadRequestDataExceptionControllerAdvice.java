package com.example.rentingservice.controlleradvice;

import com.example.rentingservice.exceptions.BadRequestDataException;
import com.example.rentingservice.models.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class BadRequestDataExceptionControllerAdvice<T extends BadRequestDataException> {

    @ResponseBody
    @ExceptionHandler(BadRequestDataException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestDataException(T exception) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST.getReasonPhrase()));
    }

}
