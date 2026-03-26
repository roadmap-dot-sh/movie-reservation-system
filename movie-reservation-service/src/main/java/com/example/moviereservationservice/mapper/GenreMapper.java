/*
 * GenreMapper.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.moviereservationservice.mapper;

import com.example.moviereservationservice.dto.response.GenreResponse;
import com.example.moviereservationservice.model.Genre;
import org.springframework.stereotype.Component;

/**
 * GenreMapper.java
 *
 * @author Nguyen
 */
@Component
public class GenreMapper {
    public GenreResponse mapToResponse(Genre genre) {
        GenreResponse response = new GenreResponse();
        response.setId(genre.getId());
        response.setName(genre.getName());
        response.setDescription(genre.getDescription());
        return response;
    }
}
