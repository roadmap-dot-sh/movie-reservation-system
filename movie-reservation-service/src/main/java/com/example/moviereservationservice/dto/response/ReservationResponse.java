/*
 * ReservationResponse.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.moviereservationservice.dto.response;

import com.example.moviereservationservice.enums.ReservationStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * ReservationResponse.java
 *
 * @author Nguyen
 */
@Data
public class ReservationResponse {
    private Long id;
    private String bookingReference;
    private String movieTitle;
    private LocalDateTime showtime;
    private String seatNumber;
    private Double totalPrice;
    private ReservationStatus status;
    private LocalDateTime reservationTime;
    private LocalDateTime expiryTime;
}
