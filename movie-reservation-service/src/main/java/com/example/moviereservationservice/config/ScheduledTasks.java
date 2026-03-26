/*
 * ScheduledTasks.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.moviereservationservice.config;

import com.example.moviereservationservice.repository.ReservationRepository;
import com.example.moviereservationservice.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ScheduledTasks.java
 *
 * @author Nguyen
 */
@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasks {
    private final ReservationRepository reservationRepository;
    private final SeatRepository seatRepository;

    // Run every minute to expire pending reservations
    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void expirePendingReservations() {
        log.info("Running scheduled task: Expiring pending reservations");
        int expiredCount = reservationRepository.expirePendingReservations();

        if (expiredCount > 0) {
            log.info("Expired {} pending reservations", expiredCount);

            // Release locked seats
            // This is handled in the expirePendingReservations query
            // but we need to update seat status as well
            // You might want to add a separate query for this
        }
    }

    // Run daily at midnight to clean up old data (optional)
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void cleanupOldData() {
        log.info("Running scheduled task: Cleaning up old data");
        // Implement cleanup logic for old reservations if needed
        // For example, archive reservations older than 1 year
    }
}
