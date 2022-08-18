package com.example.rentingservice.entities;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Reservation {
    // TODO: Add index

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer reservationId;

    @Column(nullable = false)
    private Instant start;

    @Column(nullable = false)
    private Instant end;

    private BigDecimal cost;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private ReservationObject object;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private User lessee;

}
