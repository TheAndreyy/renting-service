package com.example.rentingservice.mappres;

import com.example.rentingservice.entities.Reservation;
import com.example.rentingservice.entities.ReservationObject;
import com.example.rentingservice.entities.User;
import com.example.rentingservice.models.ReservationResponse;
import org.springframework.stereotype.Component;

@Component
public class ReservationToReservationResponseMapper {

    public ReservationResponse mapWithUserInfo(Reservation reservation) {
        User user = reservation.getLessee();
        return ReservationResponse.builder()
                .userId(user.getUserId())
                .userFirstName(user.getFirstName())
                .userLastName(user.getLastName())
                .start(reservation.getStart())
                .end(reservation.getEnd())
                .cost(reservation.getCost())
                .build();
    }

    public ReservationResponse mapWithObjectInfo(Reservation reservation) {
        ReservationObject object = reservation.getObject();
        return ReservationResponse.builder()
                .reservationObjectId(object.getReservationObjectId())
                .reservationObjectShortDescription(object.getShortDescription())
                .start(reservation.getStart())
                .end(reservation.getEnd())
                .cost(reservation.getCost())
                .build();
    }

}
