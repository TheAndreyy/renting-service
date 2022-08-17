package com.example.rentingservice.services;

import com.example.rentingservice.entities.User;
import com.example.rentingservice.mappres.reservationresponse.ReservationToReservationResponseMapper;
import com.example.rentingservice.models.reservationresponse.ReservationResponse;
import com.example.rentingservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ReservationToReservationResponseMapper reservationResponseMapper;

    public List<ReservationResponse> getReservations(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id: %d not found", userId)));
        return user.getReservations().stream()
                .map(reservationResponseMapper::map)
                .toList();
    }
}
