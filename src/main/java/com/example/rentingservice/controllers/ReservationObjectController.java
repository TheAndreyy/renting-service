package com.example.rentingservice.controllers;

import com.example.rentingservice.config.ApplicationConstants;
import com.example.rentingservice.models.CreateReservationRequest;
import com.example.rentingservice.models.UpdateReservationRequest;
import com.example.rentingservice.models.reservationresponse.ReservationResponse;
import com.example.rentingservice.services.ReservationObjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApplicationConstants.API_PREFIX + "/object")
@RequiredArgsConstructor
public class ReservationObjectController {

    private final ReservationObjectService service;

    @GetMapping("{reservationObjectId}/reservations")
    public ResponseEntity<List<ReservationResponse>> getReservations(@PathVariable Integer reservationObjectId) {
        return ResponseEntity.ok(service.getReservations(reservationObjectId));
    }

    @PostMapping("{reservationObjectId}/reserve")
    public ResponseEntity<ReservationResponse> createReservation(
            @PathVariable Integer reservationObjectId,
            @RequestBody CreateReservationRequest reservationRequest
    ) {
        return ResponseEntity.ok(service.createReservation(reservationObjectId, reservationRequest));
    }

    @PatchMapping("{reservationObjectId}/reservation/{reservationId}")
    public ResponseEntity<ReservationResponse> updateReservation(
            @PathVariable Integer reservationObjectId,
            @PathVariable Integer reservationId,
            @RequestBody UpdateReservationRequest reservationRequest) {
        return ResponseEntity.ok(service.updateReservation(reservationObjectId, reservationId, reservationRequest));
    }

}
