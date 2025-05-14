package com.floo.order_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;


@Configuration
@EnableMongoAuditing
public class MongoConfig {
    // Additional MongoDB configuration can go here if needed
}