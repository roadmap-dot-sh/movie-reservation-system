/*
 * ReservationRequest.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.moviereservationservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * ReservationRequest.java
 *
 * @author Nguyen
 */
@Data
public class ReservationRequest {
    @NotNull(message = "Showtime ID is required")
    private Long showtimeId;

    @NotBlank(message = "Seat number is required")
    private String seatNumber;
}
