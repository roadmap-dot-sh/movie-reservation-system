/*
 * PaymentRepository.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.moviereservationservice.repository;

import com.example.moviereservationservice.enums.PaymentStatus;
import com.example.moviereservationservice.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * PaymentRepository.java
 *
 * @author Nguyen
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByReservationId(Long reservationId);

    @Modifying
    @Query("UPDATE Payment p SET p.status = :status WHERE p.reservation.id = :reservationId")
    void updatePaymentStatus(@Param("reservationId") Long reservationId,
                             @Param("status") PaymentStatus status);
}
