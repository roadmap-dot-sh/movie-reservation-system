/*
 * SeatResponse.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.moviereservationservice.dto.response;

import lombok.Data;

/**
 * SeatResponse.java
 *
 * @author Nguyen
 */
@Data
public class SeatResponse {
    private Long id;
    private String seatNumber;
    private String rowNumber;
    private Integer seatColumn;
    private String status;
}
