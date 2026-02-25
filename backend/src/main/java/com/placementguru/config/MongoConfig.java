package com.placementguru.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.placementguru.repository")
public class MongoConfig {
    // MongoDB configuration is handled by application.properties
}
