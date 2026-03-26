/*
 * ReservationMapper.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.moviereservationservice.mapper;

import com.example.moviereservationservice.dto.response.ReservationResponse;
import com.example.moviereservationservice.model.Reservation;
import org.springframework.stereotype.Component;

/**
 * ReservationMapper.java
 *
 * @author Nguyen
 */
@Component
public class ReservationMapper {
    public ReservationResponse mapToResponse(Reservation reservation) {
        ReservationResponse response = new ReservationResponse();
        response.setId(reservation.getId());
        response.setBookingReference(reservation.getBookingReference());
        response.setMovieTitle(reservation.getShowtime().getMovie().getTitle());
        response.setShowtime(reservation.getShowtime().getStartTime());
        response.setSeatNumber(reservation.getSeat().getSeatNumber());
        response.setTotalPrice(reservation.getTotalPrice());
        response.setStatus(reservation.getStatus());
        response.setReservationTime(reservation.getReservationTime());
        return response;
    }
}
