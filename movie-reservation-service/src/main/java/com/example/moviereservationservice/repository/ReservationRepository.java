/*
 * ReservationRepository.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.moviereservationservice.repository;

import com.example.moviereservationservice.dto.response.MovieStatsResponse;
import com.example.moviereservationservice.enums.ReservationStatus;
import com.example.moviereservationservice.model.Reservation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * ReservationRepository.java
 *
 * @author Nguyen
 */
@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);

    Optional<Reservation> findByBookingReference(String bookingReference);

    @Query("SELECT r FROM Reservation r WHERE r.user.id = :userId AND r.showtime.startTime > :currentTime " +
            "ORDER BY r.showtime.startTime")
    List<Reservation> findByUserIdAndShowtimeStartTimeAfter(@Param("userId") Long userId,
                                                            @Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT r FROM Reservation r WHERE r.showtime.id = :showtimeId AND r.status IN :statuses")
    List<Reservation> findByShowtimeIdAndStatusIn(@Param("showtimeId") Long showtimeId,
                                                  @Param("statuses") List<ReservationStatus> statuses);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.createdAt BETWEEN :startDate AND :endDate")
    Long countByCreatedAtBetween(@Param("startDate") LocalDateTime startDate,
                                 @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.status = :status AND r.createdAt BETWEEN :startDate AND :endDate")
    Long countByStatusAndCreatedAtBetween(@Param("status") ReservationStatus status,
                                          @Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COALESCE(SUM(r.totalPrice), 0) FROM Reservation r " +
            "WHERE r.status = :status AND r.createdAt BETWEEN :startDate AND :endDate")
    Double sumTotalPriceByStatusAndCreatedAtBetween(@Param("status") ReservationStatus status,
                                                    @Param("startDate") LocalDateTime startDate,
                                                    @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.showtime.startTime BETWEEN :startDate AND :endDate " +
            "AND r.status = 'CONFIRMED'")
    Long countBookedSeatsByDateRange(@Param("startDate") LocalDateTime startDate,
                                     @Param("endDate") LocalDateTime endDate);

    @Query("SELECT new com.movie.reservation.dto.response.MovieStatsResponse(" +
            "m.id, m.title, COUNT(r), COALESCE(SUM(r.totalPrice), 0)) " +
            "FROM Reservation r JOIN r.showtime s JOIN s.movie m " +
            "WHERE r.status = 'CONFIRMED' AND r.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY m.id, m.title ORDER BY COUNT(r) DESC")
    List<MovieStatsResponse> findTopMoviesByDateRange(@Param("startDate") LocalDateTime startDate,
                                                      @Param("endDate") LocalDateTime endDate);

    @Query("SELECT HOUR(r.createdAt) as hour, COUNT(r) FROM Reservation r " +
            "WHERE r.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY HOUR(r.createdAt) ORDER BY hour")
    List<Object[]> getHourlyDistribution(@Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);

    @Modifying
    @Query("UPDATE Reservation r SET r.status = 'EXPIRED' WHERE r.status = 'PENDING_PAYMENT' " +
            "AND r.expiryTime < CURRENT_TIMESTAMP")
    int expirePendingReservations();
}
