//package com.floo.order_service.config;

//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.context.annotation.Bean;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .cors() // Enable CORS
//                .and()
//                .csrf().disable() // Disable CSRF for simplicity (not recommended for production)
//                .authorizeHttpRequests()
//                .anyRequest().permitAll();
//
//        return http.build();
//    }
//}

