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
@Table(indexes = {
        @Index(columnList = "start"),
        @Index(columnList = "end")
})
public class Reservation {

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
