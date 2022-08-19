package com.example.rentingservice.services;

import com.example.rentingservice.entities.Reservation;
import com.example.rentingservice.entities.ReservationObject;
import com.example.rentingservice.entities.User;
import com.example.rentingservice.mappres.CreateReservationRequestToReservationMapper;
import com.example.rentingservice.mappres.reservationresponse.ReservationToReservationResponseMapper;
import com.example.rentingservice.models.CreateReservationRequest;
import com.example.rentingservice.models.UpdateReservationRequest;
import com.example.rentingservice.models.reservationresponse.ReservationResponse;
import com.example.rentingservice.repositories.ReservationObjectRepository;
import com.example.rentingservice.repositories.ReservationRepository;
import com.example.rentingservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationObjectService {

    private final ReservationObjectRepository objectRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final CreateReservationRequestToReservationMapper createReservationMapper;
    private final ReservationToReservationResponseMapper reservationResponseMapper;

    public List<ReservationResponse> getReservations(Integer reservationObjectId) {
        ReservationObject object = objectRepository.findById(reservationObjectId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Object with id: %d not found", reservationObjectId)));

        return object.getReservations().stream()
                .map(reservationResponseMapper::map)
                .toList();
    }

    @Transactional
    public ReservationResponse createReservation(Integer reservationObjectId, CreateReservationRequest reservationRequest) {
        User user = userRepository.findById(reservationRequest.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id: %d not found", reservationRequest.getUserId())));

        ReservationObject object = objectRepository.findByIdWithLock(reservationObjectId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Object with id: %d not found", reservationObjectId)));

        List<Reservation> collidingReservations = reservationRepository.findAllCollidingReservations(reservationObjectId, reservationRequest.getStart(), reservationRequest.getEnd());
        if (!collidingReservations.isEmpty()) {
            throw new IllegalStateException("Object already reserved at this time.");       // TODO: Custom exception
        }
        Reservation reservation = createReservationMapper.map(reservationRequest);
        reservation.setLessee(user);
        reservation.setObject(object);
        reservation = reservationRepository.save(reservation);
        return reservationResponseMapper.map(reservation);
    }

    @Transactional
    public ReservationResponse updateReservation(Integer reservationObjectId, Integer reservationId, UpdateReservationRequest reservationRequest) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new EntityNotFoundException(String.format("Reservation with id: %d not found", reservationId)));

        Instant from = Optional.ofNullable(reservationRequest.getStart()).orElse(reservation.getStart());
        Instant to = Optional.ofNullable(reservationRequest.getEnd()).orElse(reservation.getEnd());

        objectRepository.findByIdWithLock(reservationObjectId).orElseThrow(() -> new EntityNotFoundException(String.format("Object with id: %d not found", reservationObjectId)));

        List<Reservation> collidingReservations = reservationRepository.findAllCollidingReservations(reservationObjectId, reservationRequest.getStart(), reservationRequest.getEnd());
        if (isObjectReservedByOtherReservations(reservationId, collidingReservations)) {
            throw new IllegalStateException("Object already reserved at this time.");
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
