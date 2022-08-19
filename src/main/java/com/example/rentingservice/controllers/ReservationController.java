package com.example.rentingservice.controllers;

import com.example.rentingservice.config.ApplicationConstants;
import com.example.rentingservice.models.UpdateReservationRequest;
import com.example.rentingservice.models.reservationresponse.ReservationResponse;
import com.example.rentingservice.services.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApplicationConstants.API_PREFIX + "/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService service;

    @PatchMapping("{reservationId}")                            // TODO: move to ObjectController
    public ResponseEntity<ReservationResponse> updateReservation(
            @PathVariable Integer reservationId,
            @RequestBody UpdateReservationRequest reservationRequest
    ) {
        return ResponseEntity.ok(service.updateReservation(reservationId, reservationRequest));
    }

}
