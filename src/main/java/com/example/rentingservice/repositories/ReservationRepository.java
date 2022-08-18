package com.example.rentingservice.repositories;

import com.example.rentingservice.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @Query("""
            select r from Reservation r
            where r.object.reservationObjectId = :reservationObjectId
            and (
                (r.start <= :from and :from < r.end)
                or (r.start < :to and :to <= r.end)
                )
            """)
    List<Reservation> findAllCollidingReservations(
            @Param("reservationObjectId") Integer reservationObjectId,
            @Param("from") Instant from,
            @Param("to") Instant to
    );

}
