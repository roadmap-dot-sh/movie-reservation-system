/*
 * ShowtimeResponse.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.moviereservationservice.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ShowtimeResponse.java
 *
 * @author Nguyen
 */
@Data
public class ShowtimeResponse {
    private Long id;
    private Long movieId;
    private String movieTitle;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer totalSeats;
    private Integer availableSeats;
    private Double price;
    private String hallNumber;
    private List<SeatResponse> seats;
    private boolean active;
}
