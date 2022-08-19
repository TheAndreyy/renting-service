package com.example.rentingservice.models;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class UpdateReservationRequest {

    private Instant start;
    private Instant end;
    private BigDecimal cost;

}
