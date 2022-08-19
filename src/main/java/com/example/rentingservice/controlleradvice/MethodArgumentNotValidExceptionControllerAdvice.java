package com.example.rentingservice.controlleradvice;

import com.example.rentingservice.models.ValidationErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class MethodArgumentNotValidExceptionControllerAdvice<T extends MethodArgumentNotValidException> {

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationErrorResponse>> handleMethodArgumentNotValidException(T exception) {
        return ResponseEntity.badRequest()
                .body(
                        exception.getAllErrors().stream()
                                .map(this::map)
                                .toList()
                );
    }

    private ValidationErrorResponse map(ObjectError error) {
        if (error instanceof FieldError) {
            return new ValidationErrorResponse(error.getDefaultMessage(), error.getCode(), ((FieldError) error).getField());
        }
        return new ValidationErrorResponse(error.getDefaultMessage(), error.getCode());
    }

}
