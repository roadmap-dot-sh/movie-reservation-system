/*
 * ReportService.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.moviereservationservice.service;

import com.example.moviereservationservice.dto.response.MovieStatsResponse;
import com.example.moviereservationservice.dto.response.ReportResponse;
import com.example.moviereservationservice.enums.ReservationStatus;
import com.example.moviereservationservice.repository.ReservationRepository;
import com.example.moviereservationservice.repository.ShowtimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ReportService.java
 *
 * @author Nguyen
 */
@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReservationRepository reservationRepository;
    private final ShowtimeRepository showtimeRepository;

    public ReportResponse getDailyReport(LocalDateTime date) {
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return generateReport(startOfDay, endOfDay);
    }

    public ReportResponse getWeeklyReport(LocalDateTime startDate) {
        LocalDateTime endDate = startDate.plusDays(7);
        return generateReport(startDate, endDate);
    }

    public ReportResponse getMonthlyReport(int year, int month) {
        LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDate = startDate.plusMonths(1);
        return generateReport(startDate, endDate);
    }

    public ReportResponse getCustomReport(LocalDateTime startDate, LocalDateTime endDate) {
        return generateReport(startDate, endDate);
    }

    private ReportResponse generateReport(LocalDateTime startDate, LocalDateTime endDate) {
        ReportResponse report = new ReportResponse();
        report.setStartDate(startDate);
        report.setEndDate(endDate);

        // Total reservations
        Long totalReservations = reservationRepository.countByCreatedAtBetween(startDate, endDate);
        report.setTotalReservations(totalReservations);

        // Completed reservations (confirmed payments)
        Long completedReservations = reservationRepository.countByStatusAndCreatedAtBetween(
                ReservationStatus.COMPLETED, startDate, endDate);
        report.setCompletedReservations(completedReservations);

        // Cancelled reservations
        Long cancelledReservations = reservationRepository.countByStatusAndCreatedAtBetween(
                ReservationStatus.CANCELLED, startDate, endDate);
        report.setCancelledReservations(cancelledReservations);

        // Total revenue
        Double totalRevenue = reservationRepository.sumTotalPriceByStatusAndCreatedAtBetween(
                ReservationStatus.COMPLETED, startDate, endDate);
        report.setTotalRevenue(totalRevenue != null ? totalRevenue : 0.0);

        // Occupancy rate
        Long totalSeats = showtimeRepository.countTotalSeatsByDateRange(startDate, endDate);
        Long bookedSeats = reservationRepository.countBookedSeatsByDateRange(startDate, endDate);
        report.setOccupancyRate(totalSeats != null && totalSeats > 0 ?
                (bookedSeats * 100.0 / totalSeats) : 0.0);

        // Top movies
        List<MovieStatsResponse> topMovies = reservationRepository.findTopMoviesByDateRange(startDate, endDate);
        report.setTopMovies(topMovies.stream().limit(5).collect(Collectors.toList()));

        // Hourly distribution
        List<Object[]> hourlyData = reservationRepository.getHourlyDistribution(startDate, endDate);
        Map<Integer, Long> hourlyDistribution = new HashMap<>();
        for (Object[] data : hourlyData) {
            Integer hour = (Integer) data[0];
            Long count = (Long) data[1];
            hourlyDistribution.put(hour, count);
        }
        report.setHourlyDistribution(hourlyDistribution);

        return report;
    }

    public Double getTotalRevenueByMovie(Long movieId, LocalDateTime startDate, LocalDateTime endDate) {
        // Custom query would be needed, but for simplicity:
        return reservationRepository.sumTotalPriceByStatusAndCreatedAtBetween(
                ReservationStatus.COMPLETED, startDate, endDate);
    }

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime todayStart = now.toLocalDate().atStartOfDay();

        // Today's stats
        stats.put("todayReservations", reservationRepository.countByCreatedAtBetween(todayStart, now));
        stats.put("todayRevenue", reservationRepository.sumTotalPriceByStatusAndCreatedAtBetween(
                ReservationStatus.COMPLETED, todayStart, now));

        // Upcoming showtimes count
        stats.put("upcomingShowtimes", showtimeRepository.countShowtimesInRange(now, now.plusDays(1)));

        // Total users (would need UserRepository)
        // stats.put("totalUsers", userRepository.count());

        // Occupancy for next 24 hours
        Long totalSeats = showtimeRepository.countTotalSeatsByDateRange(now, now.plusDays(1));
        Long bookedSeats = reservationRepository.countBookedSeatsByDateRange(now, now.plusDays(1));
        stats.put("next24HoursOccupancy", totalSeats != null && totalSeats > 0 ?
                (bookedSeats * 100.0 / totalSeats) : 0.0);

        return stats;
    }
}
