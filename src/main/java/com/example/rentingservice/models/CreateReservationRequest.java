package com.example.rentingservice.models;

import com.example.rentingservice.validators.StartTimeBeforeEnd;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@StartTimeBeforeEnd
public class CreateReservationRequest {

    @NotNull
    private Integer userId;

    @NotNull
    private Instant start;

    @NotNull
    private Instant end;

    @NotNull
    @PositiveOrZero
    private BigDecimal cost;

}
