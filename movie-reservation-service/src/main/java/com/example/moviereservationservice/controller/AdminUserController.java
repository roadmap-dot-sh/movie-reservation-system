/*
 * AdminUserController.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.moviereservationservice.controller;

import com.example.moviereservationservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AdminUserController.java
 *
 * @author Nguyen
 */
@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserService userService;

    @PostMapping("/{userId}/promote")
    public ResponseEntity<Void> promoteToAdmin(@PathVariable Long userId) {
        userService.promoteToAdmin(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/demote")
    public ResponseEntity<Void> demoteFromAdmin(@PathVariable Long userId) {
        userService.demoteFromAdmin(userId);
        return ResponseEntity.ok().build();
    }
}
