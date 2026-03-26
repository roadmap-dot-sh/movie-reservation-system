/*
 * ReservationController.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.moviereservationservice.controller;

import com.example.moviereservationservice.dto.request.ReservationRequest;
import com.example.moviereservationservice.dto.response.ReservationResponse;
import com.example.moviereservationservice.model.User;
import com.example.moviereservationservice.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ReservationController.java
 *
 * @author Nguyen
 */
@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            Authentication authentication,
            @RequestBody ReservationRequest request) {
        Long userId = getUserIdFromAuthentication(authentication);
        return ResponseEntity.ok(reservationService.reserveSeat(userId, request));
    }

    @PostMapping("/{reservationId}/confirm")
    public ResponseEntity<ReservationResponse> confirmReservation(
            Authentication authentication,
            @PathVariable Long reservationId) {
        Long userId = getUserIdFromAuthentication(authentication);
        return ResponseEntity.ok(reservationService.confirmReservation(reservationId, userId));
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> cancelReservation(
            Authentication authentication,
            @PathVariable Long reservationId) {
        Long userId = getUserIdFromAuthentication(authentication);
        reservationService.cancelReservation(reservationId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getUserReservations(
            Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        return ResponseEntity.ok(reservationService.getUserReservations(userId));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<ReservationResponse>> getUpcomingReservations(
            Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        return ResponseEntity.ok(reservationService.getUpcomingReservations(userId));
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        // Extract user ID from authentication details
        // This depends on your UserDetailsService implementation
        return ((User) authentication.getPrincipal()).getId();
    }
}
