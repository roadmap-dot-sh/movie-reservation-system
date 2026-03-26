/*
 * GenreService.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.moviereservationservice.service;

import com.example.moviereservationservice.dto.response.GenreResponse;
import com.example.moviereservationservice.mapper.GenreMapper;
import com.example.moviereservationservice.model.Genre;
import com.example.moviereservationservice.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * GenreService.java
 *
 * @author Nguyen
 */
@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Transactional
    public GenreResponse createGenre(String name, String description) {
        if (genreRepository.existsByName(name)) {
            throw new RuntimeException("Genre already exists");
        }

        Genre genre = new Genre();
        genre.setName(name);
        genre.setDescription(description);

        Genre savedGenre = genreRepository.save(genre);
        return genreMapper.mapToResponse(savedGenre);
    }

    public List<GenreResponse> getAllGenres() {
        return genreRepository.findAll()
                .stream()
                .map(genreMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    public GenreResponse getGenreById(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Genre not found"));
        return genreMapper.mapToResponse(genre);
    }

    @Transactional
    public void deleteGenre(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Genre not found"));
        genreRepository.delete(genre);
    }

}
