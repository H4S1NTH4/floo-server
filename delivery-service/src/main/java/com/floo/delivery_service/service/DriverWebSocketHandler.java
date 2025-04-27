package com.floo.delivery_service.service;

import com.floo.delivery_service.entity.Driver;
import com.floo.delivery_service.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DriverWebSocketHandler implements WebSocketHandler {

    private static final Map<String, WebSocketSession> driverSessions = new ConcurrentHashMap<>();

    @Autowired
    DriverRepository driverRepository;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String driverId = (String) session.getAttributes().get("driverId");
        driverSessions.put(driverId, session);
        // Mark the driver as online and available, e.g., by updating the database
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        // Process incoming messages, e.g., location updates or availability changes
        String driverId = (String) session.getAttributes().get("driverId");
        String payload = (String) message.getPayload();

        // Handle the driver location update or availability change
        // You could deserialize the payload to a JSON object
        Driver driver = driverRepository.findById(driverId).get();
        driver.setLocation(payload);  // Assuming payload contains the location data
        driverRepository.save(driver);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // Handle WebSocket errors, maybe disconnect the driver and mark them as offline
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String driverId = (String) session.getAttributes().get("driverId");
        driverSessions.remove(driverId);
        // Mark driver as offline in the database
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
