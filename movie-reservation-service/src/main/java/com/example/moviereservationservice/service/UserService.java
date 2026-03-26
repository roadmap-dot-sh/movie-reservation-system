/*
 * UserService.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.example.moviereservationservice.service;

import com.example.moviereservationservice.dto.request.LoginRequest;
import com.example.moviereservationservice.dto.request.RegisterRequest;
import com.example.moviereservationservice.dto.response.AuthResponse;
import com.example.moviereservationservice.enums.Role;
import com.example.moviereservationservice.exception.UnauthorizedException;
import com.example.moviereservationservice.model.User;
import com.example.moviereservationservice.repository.UserRepository;
import com.example.moviereservationservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserService.java
 *
 * @author Nguyen
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);

        String token = jwtUtil.generateToken(savedUser);

        return new AuthResponse(token, savedUser.getId(), savedUser.getEmail(),
                savedUser.getFullName(), savedUser.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            User user = (User) authentication.getPrincipal();
            String token = jwtUtil.generateToken(user);

            return new AuthResponse(token, user.getId(), user.getEmail(),
                    user.getFullName(), user.getRole().name());
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid email or password");
        }
    }

    @Transactional
    public void promoteToAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(Role.ADMIN);
        userRepository.save(user);
    }

    @Transactional
    public void demoteFromAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(Role.USER);
        userRepository.save(user);
    }
}
