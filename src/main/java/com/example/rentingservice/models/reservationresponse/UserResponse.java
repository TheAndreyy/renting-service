package com.example.rentingservice.models.reservationresponse;

public record UserResponse(
        Integer userId,
        String firstName,
        String lastName
) {
}
