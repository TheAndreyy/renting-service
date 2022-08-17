package com.example.rentingservice.mappres;

import com.example.rentingservice.entities.Reservation;
import com.example.rentingservice.models.CreateReservationRequest;
import org.springframework.stereotype.Component;

@Component
public class CreateReservationRequestToReservationMapper {

    public Reservation map(CreateReservationRequest request) {
        return Reservation.builder()
                .start(request.getStart())
                .end(request.getEnd())
                .cost(request.getCost())
                .build();
    }

}
