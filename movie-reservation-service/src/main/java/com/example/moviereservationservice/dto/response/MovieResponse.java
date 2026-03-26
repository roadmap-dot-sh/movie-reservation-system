/*
 * MovieResponse.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.moviereservationservice.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * MovieResponse.java
 *
 * @author Nguyen
 */
@Data
public class MovieResponse {
    private Long id;
    private String title;
    private String description;
    private String posterUrl;
    private Integer duration;
    private String language;
    private String rating;
    private LocalDateTime releaseDate;
    private List<GenreResponse> genres;
    private List<ShowtimeResponse> showtimes;
    private boolean active;
    private LocalDateTime createdAt;
}
