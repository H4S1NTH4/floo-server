package com.floo.auth_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "floo.auth")
public class AuthProperties {
    private Long refreshTokenExpiryMs;
}