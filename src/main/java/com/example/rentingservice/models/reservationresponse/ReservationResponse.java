package com.example.rentingservice.models.reservationresponse;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
public record ReservationResponse(
        Integer reservationId,
        Instant start,
        Instant end,
        BigDecimal cost,
        ObjectResponse object,
        UserResponse user
) {
}
