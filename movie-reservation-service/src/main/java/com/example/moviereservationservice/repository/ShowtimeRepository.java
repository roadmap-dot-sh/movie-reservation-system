/*
 * ShowtimeRepository.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.moviereservationservice.repository;

import com.example.moviereservationservice.model.ShowTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ShowtimeRepository.java
 *
 * @author Nguyen
 */
@Repository
public interface ShowtimeRepository extends JpaRepository<ShowTime, Long> {
    List<ShowTime> findByMovieIdAndActiveTrue(Long movieId);

    @Query("SELECT s FROM ShowTime s WHERE s.startTime BETWEEN :startDate AND :endDate " +
            "AND s.active = true ORDER BY s.startTime")
    List<ShowTime> findShowtimesByDateRange(@Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT s FROM ShowTime s WHERE s.movie.id = :movieId AND s.startTime >= :currentTime " +
            "AND s.active = true ORDER BY s.startTime")
    List<ShowTime> findUpcomingShowtimesByMovie(@Param("movieId") Long movieId,
                                                @Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT COUNT(s) FROM ShowTime s WHERE s.startTime BETWEEN :startDate AND :endDate")
    Long countShowtimesInRange(@Param("startDate") LocalDateTime startDate,
                               @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(s.totalSeats) FROM ShowTime s WHERE s.startTime BETWEEN :startDate AND :endDate")
    Long countTotalSeatsByDateRange(@Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate);

    boolean existsByHallNumberAndStartTimeBetween(String hallNumber, LocalDateTime startTime, LocalDateTime endTime);
}
