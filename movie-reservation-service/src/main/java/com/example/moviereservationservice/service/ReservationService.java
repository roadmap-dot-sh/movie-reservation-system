/*
 * ReservationService.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.moviereservationservice.service;

import com.example.moviereservationservice.dto.request.ReservationRequest;
import com.example.moviereservationservice.dto.response.ReservationResponse;
import com.example.moviereservationservice.enums.PaymentStatus;
import com.example.moviereservationservice.enums.ReservationStatus;
import com.example.moviereservationservice.enums.SeatStatus;
import com.example.moviereservationservice.exception.SeatNotAvailableException;
import com.example.moviereservationservice.mapper.ReservationMapper;
import com.example.moviereservationservice.model.*;
import com.example.moviereservationservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ReservationService.java
 *
 * @author Nguyen
 */
@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ReservationMapper reservationMapper;

    @Transactional
    public ReservationResponse reserveSeat(Long userId, ReservationRequest request) {
        // Check if showtime exists and is in the future
        ShowTime showtime = showtimeRepository.findById(request.getShowtimeId())
                .orElseThrow(() -> new RuntimeException("Showtime not found"));

        if (showtime.getStartTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Cannot reserve seats for past showtimes");
        }

        // Check if seat is available
        Seat seat = seatRepository.findByShowtimeIdAndSeatNumber(
                        request.getShowtimeId(), request.getSeatNumber())
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        if (seat.getStatus() != SeatStatus.AVAILABLE) {
            throw new SeatNotAvailableException("Seat " + request.getSeatNumber() + " is not available");
        }

        // Lock the seat
        seat.setStatus(SeatStatus.LOCKED);
        seatRepository.save(seat);

        // Create reservation
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setShowtime(showtime);
        reservation.setSeat(seat);
        reservation.setTotalPrice(showtime.getPrice());
        reservation.setStatus(ReservationStatus.PENDING_PAYMENT);

        Reservation savedReservation = reservationRepository.save(reservation);

        // Send confirmation email
        emailService.sendReservationConfirmation(user.getEmail(), savedReservation);

        return reservationMapper.mapToResponse(savedReservation);
    }

    @Transactional
    public ReservationResponse confirmReservation(Long reservationId, Long userId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (!reservation.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You don't own this reservation");
        }

        if (reservation.getExpiryTime().isBefore(LocalDateTime.now())) {
            // Release the seat
            Seat seat = reservation.getSeat();
            seat.setStatus(SeatStatus.AVAILABLE);
            seatRepository.save(seat);

            reservation.setStatus(ReservationStatus.EXPIRED);
            reservationRepository.save(reservation);

            throw new RuntimeException("Reservation has expired");
        }

        // Process payment (simplified)
        Payment payment = new Payment();
        payment.setReservation(reservation);
        payment.setAmount(reservation.getTotalPrice());
        payment.setStatus(PaymentStatus.SUCCESS);
        reservation.setPayment(payment);
        reservation.setStatus(ReservationStatus.CONFIRMED);

        // Book the seat
        Seat seat = reservation.getSeat();
        seat.setStatus(SeatStatus.BOOKED);
        seatRepository.save(seat);

        Reservation confirmedReservation = reservationRepository.save(reservation);

        return reservationMapper.mapToResponse(confirmedReservation);
    }

    @Transactional
    public void cancelReservation(Long reservationId, Long userId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (!reservation.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You don't own this reservation");
        }

        if (reservation.getShowtime().getStartTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Cannot cancel past reservations");
        }

        // Release the seat
        Seat seat = reservation.getSeat();
        seat.setStatus(SeatStatus.AVAILABLE);
        seatRepository.save(seat);

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        // Send cancellation email
        emailService.sendReservationCancellation(reservation.getUser().getEmail(), reservation);
    }

    public List<ReservationResponse> getUserReservations(Long userId) {
        return reservationRepository.findByUserId(userId)
                .stream()
                .map(reservationMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ReservationResponse> getUpcomingReservations(Long userId) {
        return reservationRepository.findByUserIdAndShowtimeStartTimeAfter(
                        userId, LocalDateTime.now())
                .stream()
                .map(reservationMapper::mapToResponse)
                .collect(Collectors.toList());
    }
}
