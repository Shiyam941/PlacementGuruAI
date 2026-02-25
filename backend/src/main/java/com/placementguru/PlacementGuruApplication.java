package com.placementguru;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class PlacementGuruApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlacementGuruApplication.class, args);
        System.out.println("PlacementGuru AI Platform is running...");
    }
}
