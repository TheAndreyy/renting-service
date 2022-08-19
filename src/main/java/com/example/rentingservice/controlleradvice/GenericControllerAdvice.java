package com.example.rentingservice.controlleradvice;

import com.example.rentingservice.models.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Order
@ControllerAdvice
public class GenericControllerAdvice<T extends RuntimeException> {

    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeErrorException(T exception) {
        log.error("Internal server error!", exception);
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse("UnexpectedError", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
    }

}
