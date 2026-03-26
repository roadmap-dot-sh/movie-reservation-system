/*
 * ShowtimeService.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.moviereservationservice.service;

import com.example.moviereservationservice.dto.request.ShowtimeRequest;
import com.example.moviereservationservice.dto.response.ShowtimeResponse;
import com.example.moviereservationservice.enums.ReservationStatus;
import com.example.moviereservationservice.enums.SeatStatus;
import com.example.moviereservationservice.mapper.ShowtimeMapper;
import com.example.moviereservationservice.model.Movie;
import com.example.moviereservationservice.model.Reservation;
import com.example.moviereservationservice.model.Seat;
import com.example.moviereservationservice.model.ShowTime;
import com.example.moviereservationservice.repository.MovieRepository;
import com.example.moviereservationservice.repository.ReservationRepository;
import com.example.moviereservationservice.repository.SeatRepository;
import com.example.moviereservationservice.repository.ShowtimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * ShowtimeService.java
 *
 * @author Nguyen
 */
@Service
@RequiredArgsConstructor
public class ShowtimeService {
    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final SeatRepository seatRepository;
    private final ReservationRepository reservationRepository;
    private final ShowtimeMapper showtimeMapper;

    @Transactional
    public ShowtimeResponse createShowtime(ShowtimeRequest request) {
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        // Check for scheduling conflicts
        boolean conflict = showtimeRepository.existsByHallNumberAndStartTimeBetween(
                request.getHallNumber(),
                request.getStartTime(),
                request.getEndTime()
        );

        if (conflict) {
            throw new RuntimeException("Hall is already booked for this time slot");
        }

        ShowTime showtime = new ShowTime();
        showtime.setMovie(movie);
        showtime.setStartTime(request.getStartTime());
        showtime.setEndTime(request.getEndTime());
        showtime.setTotalSeats(request.getTotalSeats());
        showtime.setPrice(request.getPrice());
        showtime.setHallNumber(request.getHallNumber());

        ShowTime savedShowtime = showtimeRepository.save(showtime);

        // Create seats
        createSeatsForShowtime(savedShowtime);

        return showtimeMapper.mapToResponse(savedShowtime);
    }

    private void createSeatsForShowtime(ShowTime showtime) {
        int totalSeats = showtime.getTotalSeats();
        int rows = (int) Math.ceil(totalSeats / 10.0);

        List<Seat> seats = IntStream.range(0, totalSeats)
                .mapToObj(i -> {
                    Seat seat = new Seat();
                    seat.setShowtime(showtime);
                    int rowNumber = i / 10;
                    int columnNumber = i % 10;
                    char rowChar = (char) ('A' + rowNumber);
                    seat.setSeatNumber(String.format("%c%d", rowChar, columnNumber + 1));
                    seat.setRowNumber(String.valueOf(rowChar));
                    seat.setSeatColumn(columnNumber + 1);
                    seat.setStatus(SeatStatus.AVAILABLE);
                    return seat;
                })
                .collect(Collectors.toList());

        seatRepository.saveAll(seats);
    }

    public ShowtimeResponse getShowtimeById(Long id) {
        ShowTime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Showtime not found"));
        return showtimeMapper.mapToResponse(showtime);
    }

    public List<ShowtimeResponse> getShowtimesByMovie(Long movieId) {
        return showtimeRepository.findByMovieIdAndActiveTrue(movieId)
                .stream()
                .map(showtimeMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ShowtimeResponse> getShowtimesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return showtimeRepository.findShowtimesByDateRange(startDate, endDate)
                .stream()
                .map(showtimeMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelShowtime(Long id) {
        ShowTime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Showtime not found"));

        // Check if there are any confirmed reservations
        List<Reservation> confirmedReservations = reservationRepository
                .findByShowtimeIdAndStatusIn(id, List.of(ReservationStatus.CONFIRMED, ReservationStatus.PENDING_PAYMENT));

        if (!confirmedReservations.isEmpty()) {
            throw new RuntimeException("Cannot cancel showtime with existing reservations");
        }

        showtime.setActive(false);
        showtimeRepository.save(showtime);
    }
}
