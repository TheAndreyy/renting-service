package com.example.rentingservice.services;

import com.example.rentingservice.entities.Reservation;
import com.example.rentingservice.entities.ReservationObject;
import com.example.rentingservice.entities.User;
import com.example.rentingservice.mappres.CreateReservationRequestToReservationMapper;
import com.example.rentingservice.models.CreateReservationRequest;
import com.example.rentingservice.repositories.ReservationObjectRepository;
import com.example.rentingservice.repositories.ReservationRepository;
import com.example.rentingservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class ReservationObjectService {

    private final ReservationObjectRepository objectRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final CreateReservationRequestToReservationMapper reservationMapper;

    public void createReservation(Integer reservationObjectId, CreateReservationRequest reservationRequest) {
        User user = userRepository.findById(reservationRequest.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id: %d not found", reservationRequest.getUserId())));

        ReservationObject object = objectRepository.findById(reservationObjectId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Object with id: %d not found", reservationObjectId)));

        // TODO: check if free
        Reservation reservation = reservationMapper.map(reservationRequest);
        reservation.setLessee(user);
        reservation.setObject(object);
        reservationRepository.save(reservation);
    }

}
