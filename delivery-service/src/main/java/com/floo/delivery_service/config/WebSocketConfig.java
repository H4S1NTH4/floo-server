package com.floo.delivery_service.config;

import com.floo.delivery_service.service.DriverWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private DriverWebSocketHandler driverWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(driverWebSocketHandler, "/ws/driver/{driverId}")
                .setAllowedOrigins("*");
    }

//    @Bean
//    public WebSocketHandler driverWebSocketHandler() {
//        return new DriverWebSocketHandler();
//    }
}