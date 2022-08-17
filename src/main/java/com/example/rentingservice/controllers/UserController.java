package com.example.rentingservice.controllers;

import com.example.rentingservice.config.ApplicationConstants;
import com.example.rentingservice.models.reservationresponse.ReservationResponse;
import com.example.rentingservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApplicationConstants.API_PREFIX + "/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("{userId}/reservations")
    public ResponseEntity<List<ReservationResponse>> getReservations(@PathVariable Integer userId) {
        return ResponseEntity.ok(userService.getReservations(userId));
    }

}
