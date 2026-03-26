/*
 * GenreResponse.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.moviereservationservice.dto.response;

import lombok.Data;

/**
 * GenreResponse.java
 *
 * @author Nguyen
 */
@Data
public class GenreResponse {
    private Long id;
    private String name;
    private String description;
}
