/*
 * MovieMapper.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.moviereservationservice.mapper;

import com.example.moviereservationservice.dto.response.GenreResponse;
import com.example.moviereservationservice.dto.response.MovieResponse;
import com.example.moviereservationservice.model.Movie;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * MovieMapper.java
 *
 * @author Nguyen
 */
@Component
public class MovieMapper {
    private GenreMapper genreMapper;

    public MovieResponse mapToResponse(Movie movie) {
        MovieResponse response = new MovieResponse();
        response.setId(movie.getId());
        response.setTitle(movie.getTitle());
        response.setDescription(movie.getDescription());
        response.setPosterUrl(movie.getPosterUrl());
        response.setDuration(movie.getDuration());
        response.setLanguage(movie.getLanguage());
        response.setRating(movie.getRating());
        response.setReleaseDate(movie.getReleaseDate());
        response.setActive(movie.isActive());
        response.setCreatedAt(movie.getCreatedAt());

        if (movie.getGenres() != null) {
            List<GenreResponse> genreResponses = movie.getGenres().stream()
                    .map(genreMapper::mapToResponse)
                    .collect(Collectors.toList());
            response.setGenres(genreResponses);
        }

        return response;
    }
}
