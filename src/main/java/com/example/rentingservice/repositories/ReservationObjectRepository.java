package com.example.rentingservice.repositories;

import com.example.rentingservice.entities.ReservationObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface ReservationObjectRepository extends JpaRepository<ReservationObject, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select ro from ReservationObject ro where ro.reservationObjectId = :id")
    Optional<ReservationObject> findByIdWithLock(@Param("id") Integer id);

}
