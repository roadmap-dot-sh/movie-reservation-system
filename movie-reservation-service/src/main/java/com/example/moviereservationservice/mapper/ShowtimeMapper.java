/*
 * ShowtimeMapper.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.moviereservationservice.mapper;

import com.example.moviereservationservice.dto.response.SeatResponse;
import com.example.moviereservationservice.dto.response.ShowtimeResponse;
import com.example.moviereservationservice.enums.SeatStatus;
import com.example.moviereservationservice.model.ShowTime;
import com.example.moviereservationservice.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ShowtimeMapper.java
 *
 * @author Nguyen
 */
@Component
public class ShowtimeMapper {
    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private SeatMapper seatMapper;

    public ShowtimeResponse mapToResponse(ShowTime showtime) {
        ShowtimeResponse response = new ShowtimeResponse();
        response.setId(showtime.getId());
        response.setMovieId(showtime.getMovie().getId());
        response.setMovieTitle(showtime.getMovie().getTitle());
        response.setStartTime(showtime.getStartTime());
        response.setEndTime(showtime.getEndTime());
        response.setTotalSeats(showtime.getTotalSeats());
        response.setPrice(showtime.getPrice());
        response.setHallNumber(showtime.getHallNumber());
        response.setActive(showtime.isActive());

        // Get available seats count
        Long availableSeats = seatRepository.countByShowtimeIdAndStatus(
                showtime.getId(), SeatStatus.AVAILABLE);
        response.setAvailableSeats(availableSeats.intValue());

        // Get seat details
        List<SeatResponse> seatResponses = seatRepository.findByShowtimeId(showtime.getId())
                .stream()
                .map(seatMapper::mapSeatToResponse)
                .collect(Collectors.toList());
        response.setSeats(seatResponses);

        return response;
    }
}
