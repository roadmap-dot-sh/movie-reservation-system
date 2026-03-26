/*
 * SeatRepository.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.moviereservationservice.repository;

import com.example.moviereservationservice.enums.SeatStatus;
import com.example.moviereservationservice.model.Seat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * SeatRepository.java
 *
 * @author Nguyen
 */
@Repository
public interface SeatRepository extends CrudRepository<Seat, Long> {
    List<Seat> findByShowtimeId(Long showtimeId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Seat s WHERE s.showtime.id = :showtimeId AND s.seatNumber = :seatNumber")
    Optional<Seat> findByShowtimeIdAndSeatNumberWithLock(@Param("showtimeId") Long showtimeId,
                                                         @Param("seatNumber") String seatNumber);

    default Optional<Seat> findByShowtimeIdAndSeatNumber(Long showtimeId, String seatNumber) {
        return findByShowtimeIdAndSeatNumberWithLock(showtimeId, seatNumber);
    }

    @Query("SELECT s FROM Seat s WHERE s.showtime.id = :showtimeId AND s.status = :status")
    List<Seat> findByShowtimeIdAndStatus(@Param("showtimeId") Long showtimeId,
                                         @Param("status") SeatStatus status);

    @Query("SELECT COUNT(s) FROM Seat s WHERE s.showtime.id = :showtimeId AND s.status = :status")
    Long countByShowtimeIdAndStatus(@Param("showtimeId") Long showtimeId,
                                    @Param("status") SeatStatus status);

    @Modifying
    @Query("UPDATE Seat s SET s.status = :status WHERE s.id = :seatId")
    void updateSeatStatus(@Param("seatId") Long seatId, @Param("status") SeatStatus status);

    @Modifying
    @Query("UPDATE Seat s SET s.status = 'AVAILABLE' WHERE s.showtime.id = :showtimeId " +
            "AND s.status = 'LOCKED' AND s.id IN (SELECT r.seat.id FROM Reservation r " +
            "WHERE r.expiryTime < CURRENT_TIMESTAMP AND r.status = 'PENDING_PAYMENT')")
    int releaseExpiredLocks(@Param("showtimeId") Long showtimeId);
}
