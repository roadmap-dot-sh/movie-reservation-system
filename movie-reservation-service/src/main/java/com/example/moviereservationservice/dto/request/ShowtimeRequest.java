/*
 * ShowtimeRequest.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.moviereservationservice.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * ShowtimeRequest.java
 *
 * @author Nguyen
 */
@Data
public class ShowtimeRequest {
    @NotNull(message = "Movie ID is required")
    private Long movieId;

    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in the future")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @Future(message = "End time must be in the future")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endTime;

    @NotNull(message = "Total seats is required")
    @Positive(message = "Total seats must be positive")
    private Integer totalSeats;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;

    private String hallNumber;
}
