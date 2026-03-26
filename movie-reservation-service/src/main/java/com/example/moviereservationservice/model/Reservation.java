/*
 * Reservation.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.moviereservationservice.model;

import com.example.moviereservationservice.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Reservation.java
 *
 * @author Nguyen
 */
@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "showtime_id", nullable = false)
    private ShowTime showtime;

    @OneToOne
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @Column(unique = true)
    private String bookingReference;

    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.CONFIRMED;

    private LocalDateTime reservationTime;
    private LocalDateTime expiryTime; // For payment window

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private Payment payment;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (reservationTime == null) {
            reservationTime = LocalDateTime.now();
        }
        if (expiryTime == null) {
            expiryTime = reservationTime.plusMinutes(15); // 15 minutes to complete payment
        }
        if (bookingReference == null) {
            bookingReference = generateBookingReference();
        }
    }

    private String generateBookingReference() {
        return "BK" + System.currentTimeMillis() + id;
    }
}
