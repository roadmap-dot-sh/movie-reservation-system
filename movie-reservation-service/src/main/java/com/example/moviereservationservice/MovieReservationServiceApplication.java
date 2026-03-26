package com.example.moviereservationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MovieReservationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieReservationServiceApplication.class, args);
    }

}
