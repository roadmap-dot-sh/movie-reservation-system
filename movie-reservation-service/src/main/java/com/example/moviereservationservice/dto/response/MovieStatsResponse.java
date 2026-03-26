/*
 * MovieStatsResponse.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.moviereservationservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * MovieStatsResponse.java
 *
 * @author Nguyen
 */
@Data
@AllArgsConstructor
public class MovieStatsResponse {
    private Long movieId;
    private String movieTitle;
    private Long reservationCount;
    private Double revenue;
}
