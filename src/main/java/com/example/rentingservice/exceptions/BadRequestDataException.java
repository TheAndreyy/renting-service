package com.example.rentingservice.exceptions;

public abstract class BadRequestDataException extends RuntimeException {

    public BadRequestDataException(String message) {
        super(message);
    }

}
