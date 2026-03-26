/*
 * Seat.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.moviereservationservice.model;

import com.example.moviereservationservice.enums.SeatStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Seat.java
 *
 * @author Nguyen
 */
@Entity
@Table(name = "seats", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"showtime_id", "seatNumber"})
})
@Data
@NoArgsConstructor
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "showtime_id", nullable = false)
    private ShowTime showtime;

    @Column(nullable = false)
    private String seatNumber; // e.g., "A1", "B5"

    private String rowNumber;
    private Integer seatColumn;

    @Enumerated(EnumType.STRING)
    private SeatStatus status = SeatStatus.AVAILABLE;

    @OneToOne(mappedBy = "seat")
    private Reservation reservation;
}
