package com.example.rentingservice.mappres.reservationresponse;

import com.example.rentingservice.entities.Reservation;
import com.example.rentingservice.models.reservationresponse.ReservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationToReservationResponseMapper {

    private final UserToUserResponseMapper userMapper;
    private final ReservationObjectToObjectResponseMapper objectMapper;

    public ReservationResponse map(Reservation reservation) {
        return ReservationResponse.builder()
                .user(userMapper.map(reservation.getLessee()))
                .object(objectMapper.map(reservation.getObject()))
                .start(reservation.getStart())
                .end(reservation.getEnd())
                .cost(reservation.getCost())
                .build();
    }

}
