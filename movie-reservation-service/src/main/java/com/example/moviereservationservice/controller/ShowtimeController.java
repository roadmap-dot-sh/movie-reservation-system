/*
 * ShowtimeController.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.moviereservationservice.controller;

import com.example.moviereservationservice.dto.request.ShowtimeRequest;
import com.example.moviereservationservice.dto.response.ShowtimeResponse;
import com.example.moviereservationservice.service.ShowtimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ShowtimeController.java
 *
 * @author Nguyen
 */
@RestController
@RequestMapping("/api/showtimes")
@RequiredArgsConstructor
public class ShowtimeController {
    private final ShowtimeService showtimeService;

    @GetMapping("/{id}")
    public ResponseEntity<ShowtimeResponse> getShowtimeById(@PathVariable Long id) {
        return ResponseEntity.ok(showtimeService.getShowtimeById(id));
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ShowtimeResponse>> getShowtimesByMovie(@PathVariable Long movieId) {
        return ResponseEntity.ok(showtimeService.getShowtimesByMovie(movieId));
    }

    @GetMapping
    public ResponseEntity<List<ShowtimeResponse>> getShowtimesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(showtimeService.getShowtimesByDateRange(startDate, endDate));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShowtimeResponse> createShowtime(@Valid @RequestBody ShowtimeRequest request) {
        return ResponseEntity.ok(showtimeService.createShowtime(request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> cancelShowtime(@PathVariable Long id) {
        showtimeService.cancelShowtime(id);
        return ResponseEntity.noContent().build();
    }
}
