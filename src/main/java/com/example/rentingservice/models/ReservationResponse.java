package com.example.rentingservice.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReservationResponse(
        Integer userId,
        String userFirstName,
        String userLastName,
        Integer reservationObjectId,
        String reservationObjectShortDescription,
        Instant start,
        Instant end,
        BigDecimal cost
) {
}
