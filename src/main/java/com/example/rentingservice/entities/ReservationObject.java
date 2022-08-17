package com.example.rentingservice.entities;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ReservationObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer reservationObjectId;

    @Column(nullable = false, length = 100)
    private String shortDescription;
    private String longDescription;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TimeUnit unit;

    @Setter(AccessLevel.NONE)
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private User owner;

    @OneToMany(mappedBy = "object", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations = new java.util.ArrayList<>();

}
