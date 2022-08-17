package com.example.rentingservice.controllers;

import com.example.rentingservice.config.ApplicationConstants;
import com.example.rentingservice.models.CreateReservationRequest;
import com.example.rentingservice.services.ReservationObjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApplicationConstants.API_PREFIX + "/object")
@RequiredArgsConstructor
public class ReservationObjectController {

    private final ReservationObjectService service;

    @PostMapping("{reservationObjectId}/reserve")
    public ResponseEntity<Void> createReservation(
            @PathVariable Integer reservationObjectId,
            @RequestBody CreateReservationRequest reservationRequest
    ) {
        service.createReservation(reservationObjectId, reservationRequest);
        return ResponseEntity.noContent().build();
    }

}
