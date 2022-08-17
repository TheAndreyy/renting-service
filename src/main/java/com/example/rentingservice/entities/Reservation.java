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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long reservationId;

    @Column(nullable = false)
    private Instant from;

    @Column(nullable = false)
    private Instant to;

    private BigDecimal cost;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private ReservationObject object;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private User lessee;

}
