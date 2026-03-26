/*
 * EmailService.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.moviereservationservice.service;

import com.example.moviereservationservice.model.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * EmailService.java
 *
 * @author Nguyen
 */
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendReservationConfirmation(String email, Reservation reservation) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Reservation Confirmation - " + reservation.getBookingReference());
        message.setText(String.format(
                "Dear %s,\n\nYour reservation has been confirmed.\n\n" +
                        "Movie: %s\n" +
                        "Showtime: %s\n" +
                        "Seat: %s\n" +
                        "Booking Reference: %s\n\n" +
                        "Thank you for choosing our service!",
                reservation.getUser().getFullName(),
                reservation.getShowtime().getMovie().getTitle(),
                reservation.getShowtime().getStartTime(),
                reservation.getSeat().getSeatNumber(),
                reservation.getBookingReference()
        ));

        mailSender.send(message);
    }

    public void sendReservationCancellation(String email, Reservation reservation) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Reservation Cancelled - " + reservation.getBookingReference());
        message.setText(String.format(
                "Dear %s,\n\nYour reservation has been cancelled.\n\n" +
                        "Booking Reference: %s\n\n" +
                        "We hope to see you soon!",
                reservation.getUser().getFullName(),
                reservation.getBookingReference()
        ));

        mailSender.send(message);
    }
}
