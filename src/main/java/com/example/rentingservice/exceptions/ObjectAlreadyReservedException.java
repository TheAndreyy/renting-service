package com.example.rentingservice.exceptions;

public class ObjectAlreadyReservedException extends BadRequestDataException {

    public ObjectAlreadyReservedException(String s) {
        super(s);
    }

}
