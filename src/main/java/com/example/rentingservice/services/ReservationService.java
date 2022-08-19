package com.example.rentingservice.services;

import com.example.rentingservice.entities.Reservation;
import com.example.rentingservice.mappres.reservationresponse.ReservationToReservationResponseMapper;
import com.example.rentingservice.models.UpdateReservationRequest;
import com.example.rentingservice.models.reservationresponse.ReservationResponse;
import com.example.rentingservice.repositories.ReservationObjectRepository;
import com.example.rentingservice.repositories.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationObjectRepository objectRepository;
    private final ReservationToReservationResponseMapper reservationResponseMapper;

    @Transactional
    public ReservationResponse updateReservation(Integer reservationId, UpdateReservationRequest reservationRequest) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new EntityNotFoundException(String.format("Reservation with id: %d not found", reservationId)));

        Instant from = Optional.ofNullable(reservationRequest.getStart()).orElse(reservation.getStart());
        Instant to = Optional.ofNullable(reservationRequest.getEnd()).orElse(reservation.getEnd());

        Integer reservationObjectId = reservation.getObject().getReservationObjectId();
        objectRepository.findByIdWithLock(reservationObjectId).orElseThrow(() -> new EntityNotFoundException(String.format("Object with id: %d not found", reservationObjectId)));

        List<Reservation> collidingReservations = reservationRepository.findAllCollidingReservations(reservationObjectId, reservationRequest.getStart(), reservationRequest.getEnd());
        if (isObjectReservedByOtherReservations(reservationId, collidingReservations)) {
            throw new IllegalStateException("Object already reserved at this time.");       // TODO: Custom exception
        }

        reservation.setStart(from);
        reservation.setEnd(to);
        Optional.ofNullable(reservationRequest.getCost()).ifPresent(reservation::setCost);
        return reservationResponseMapper.map(reservation);
    }

    private static boolean isObjectReservedByOtherReservations(Integer reservationId, List<Reservation> collidingReservations) {
        return collidingReservations.size() > 1 || (
                collidingReservations.size() == 1 && !collidingReservations.get(0).getReservationId().equals(reservationId)
        );
    }

}
