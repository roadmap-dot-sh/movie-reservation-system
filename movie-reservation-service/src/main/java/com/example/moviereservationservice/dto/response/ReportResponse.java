/*
 * ReportResponse.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.moviereservationservice.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * ReportResponse.java
 *
 * @author Nguyen
 */
@Data
public class ReportResponse {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long totalReservations;
    private Long completedReservations;
    private Long cancelledReservations;
    private Double totalRevenue;
    private Double occupancyRate;
    private List<MovieStatsResponse> topMovies;
    private Map<Integer, Long> hourlyDistribution;
}
