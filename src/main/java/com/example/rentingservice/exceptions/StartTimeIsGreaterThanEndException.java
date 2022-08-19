package com.example.rentingservice.exceptions;

public class StartTimeIsGreaterThanEndException extends BadRequestDataException {

    public StartTimeIsGreaterThanEndException(String s) {
        super(s);
    }

}
