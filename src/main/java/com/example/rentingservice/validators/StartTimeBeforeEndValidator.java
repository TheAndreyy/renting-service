package com.example.rentingservice.validators;

import com.example.rentingservice.models.CreateReservationRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StartTimeBeforeEndValidator implements ConstraintValidator<StartTimeBeforeEnd, CreateReservationRequest> {


    @Override
    public boolean isValid(CreateReservationRequest request, ConstraintValidatorContext context) {
        return request.getStart().compareTo(request.getEnd()) <= 0;
    }

}
