/*
 * AdminReportController.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.moviereservationservice.controller;

import com.example.moviereservationservice.dto.response.ReportResponse;
import com.example.moviereservationservice.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * AdminReportController.java
 *
 * @author Nguyen
 */
@RestController
@RequestMapping("/api/admin/reports")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminReportController {
    private final ReportService reportService;

    @GetMapping("/daily")
    public ResponseEntity<ReportResponse> getDailyReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        return ResponseEntity.ok(reportService.getDailyReport(date));
    }

    @GetMapping("/weekly")
    public ResponseEntity<ReportResponse> getWeeklyReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate) {
        return ResponseEntity.ok(reportService.getWeeklyReport(startDate));
    }

    @GetMapping("/monthly")
    public ResponseEntity<ReportResponse> getMonthlyReport(
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(reportService.getMonthlyReport(year, month));
    }
}
