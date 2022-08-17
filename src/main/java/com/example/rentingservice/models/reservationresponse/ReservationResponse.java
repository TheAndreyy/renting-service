package com.example.rentingservice.models.reservationresponse;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
public record ReservationResponse(
        UserResponse user,
        ObjectResponse object,
        Instant start,
        Instant end,
        BigDecimal cost
) {
}
