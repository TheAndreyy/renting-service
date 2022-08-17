package com.example.rentingservice.repositories;

import com.example.rentingservice.entities.ReservationObject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationObjectRepository extends JpaRepository<ReservationObject, Integer> {
}
