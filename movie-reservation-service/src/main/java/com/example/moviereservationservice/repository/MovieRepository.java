/*
 * MovieRepository.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.moviereservationservice.repository;

import com.example.moviereservationservice.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * MovieRepository.java
 *
 * @author Nguyen
 */
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByActiveTrue();

    Page<Movie> findByActiveTrue(Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE m.active = true AND " +
            "(:title IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:genreId IS NULL OR EXISTS (SELECT g FROM m.genres g WHERE g.id = :genreId))")
    Page<Movie> searchMovies(@Param("title") String title,
                             @Param("genreId") Long genreId,
                             Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE m.releaseDate <= :currentDate AND m.active = true " +
            "ORDER BY m.releaseDate DESC")
    List<Movie> findNowShowing(@Param("currentDate") LocalDateTime currentDate);

    @Query("SELECT m FROM Movie m WHERE m.releaseDate > :currentDate AND m.active = true " +
            "ORDER BY m.releaseDate ASC")
    List<Movie> findUpcoming(@Param("currentDate") LocalDateTime currentDate);
}
