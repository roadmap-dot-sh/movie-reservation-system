/*
 * SeatMapper.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.moviereservationservice.mapper;

import com.example.moviereservationservice.dto.response.SeatResponse;
import com.example.moviereservationservice.model.Seat;
import org.springframework.stereotype.Component;

/**
 * SeatMapper.java
 *
 * @author Nguyen
 */
@Component
public class SeatMapper {
    public SeatResponse mapSeatToResponse(Seat seat) {
        SeatResponse response = new SeatResponse();
        response.setId(seat.getId());
        response.setSeatNumber(seat.getSeatNumber());
        response.setRowNumber(seat.getRowNumber());
        response.setSeatColumn(seat.getSeatColumn());
        response.setStatus(seat.getStatus().name());
        return response;
    }
}
