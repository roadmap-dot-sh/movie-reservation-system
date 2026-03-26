/*
 * MovieService.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.moviereservationservice.service;

import com.example.moviereservationservice.dto.request.MovieRequest;
import com.example.moviereservationservice.dto.response.MovieResponse;
import com.example.moviereservationservice.mapper.MovieMapper;
import com.example.moviereservationservice.model.Genre;
import com.example.moviereservationservice.model.Movie;
import com.example.moviereservationservice.repository.GenreRepository;
import com.example.moviereservationservice.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MovieService.java
 *
 * @author Nguyen
 */
@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final MovieMapper movieMapper;

    @Transactional
    public MovieResponse createMovie(MovieRequest request) {
        Movie movie = new Movie();
        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setPosterUrl(request.getPosterUrl());
        movie.setDuration(request.getDuration());
        movie.setLanguage(request.getLanguage());
        movie.setRating(request.getRating());
        movie.setReleaseDate(request.getReleaseDate());

        if (request.getGenreIds() != null) {
            List<Genre> genres = genreRepository.findAllById(request.getGenreIds());
            movie.setGenres(genres);
        }

        Movie savedMovie = movieRepository.save(movie);
        return movieMapper.mapToResponse(savedMovie);
    }

    @Transactional
    public MovieResponse updateMovie(Long id, MovieRequest request) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setPosterUrl(request.getPosterUrl());
        movie.setDuration(request.getDuration());
        movie.setLanguage(request.getLanguage());
        movie.setRating(request.getRating());
        movie.setReleaseDate(request.getReleaseDate());

        if (request.getGenreIds() != null) {
            List<Genre> genres = genreRepository.findAllById(request.getGenreIds());
            movie.setGenres(genres);
        }

        Movie updatedMovie = movieRepository.save(movie);
        return movieMapper.mapToResponse(updatedMovie);
    }

    @Transactional
    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        movie.setActive(false);
        movieRepository.save(movie);
    }

    public MovieResponse getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        return movieMapper.mapToResponse(movie);
    }

    public Page<MovieResponse> getAllMovies(Pageable pageable) {
        return movieRepository.findByActiveTrue(pageable)
                .map(movieMapper::mapToResponse);
    }

    public Page<MovieResponse> searchMovies(String title, Long genreId, Pageable pageable) {
        return movieRepository.searchMovies(title, genreId, pageable)
                .map(movieMapper::mapToResponse);
    }

    public List<MovieResponse> getNowShowing() {
        return movieRepository.findNowShowing(LocalDateTime.now())
                .stream()
                .map(movieMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<MovieResponse> getUpcoming() {
        return movieRepository.findUpcoming(LocalDateTime.now())
                .stream()
                .map(movieMapper::mapToResponse)
                .collect(Collectors.toList());
    }
}
