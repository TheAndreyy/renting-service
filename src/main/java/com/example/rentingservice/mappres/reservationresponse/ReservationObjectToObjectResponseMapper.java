package com.example.rentingservice.mappres.reservationresponse;

import com.example.rentingservice.entities.ReservationObject;
import com.example.rentingservice.models.reservationresponse.ObjectResponse;
import org.springframework.stereotype.Component;

@Component
class ReservationObjectToObjectResponseMapper {

    ObjectResponse map(ReservationObject object) {
        return new ObjectResponse(object.getReservationObjectId(), object.getShortDescription());
    }
}
