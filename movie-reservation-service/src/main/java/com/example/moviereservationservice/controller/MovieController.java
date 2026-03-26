/*
 * MovieController.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.moviereservationservice.controller;

import com.example.moviereservationservice.dto.request.MovieRequest;
import com.example.moviereservationservice.dto.response.MovieResponse;
import com.example.moviereservationservice.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * MovieController.java
 *
 * @author Nguyen
 */
@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping
    public ResponseEntity<Page<MovieResponse>> getAllMovies(
            @PageableDefault(size = 20, sort = "releaseDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(movieService.getAllMovies(pageable));
    }

    @GetMapping("/now-showing")
    public ResponseEntity<List<MovieResponse>> getNowShowing() {
        return ResponseEntity.ok(movieService.getNowShowing());
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<MovieResponse>> getUpcoming() {
        return ResponseEntity.ok(movieService.getUpcoming());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<MovieResponse>> searchMovies(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long genreId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(movieService.searchMovies(title, genreId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponse> getMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieResponse> createMovie(@Valid @RequestBody MovieRequest request) {
        return ResponseEntity.ok(movieService.createMovie(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieResponse> updateMovie(@PathVariable Long id,
                                                     @Valid @RequestBody MovieRequest request) {
        return ResponseEntity.ok(movieService.updateMovie(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}
