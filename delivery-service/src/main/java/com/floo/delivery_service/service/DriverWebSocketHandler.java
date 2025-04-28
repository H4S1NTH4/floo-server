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
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DriverWebSocketHandler implements WebSocketHandler {

    //session storage with driverId as the key
    protected static final Map<String, WebSocketSession> driverSessions = new ConcurrentHashMap<>();
    // In-memory storage of driver objects with driverId as the key
    private static final Map<String, Driver> driverMemory = new ConcurrentHashMap<>();


    @Autowired
    DriverRepository driverRepository;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //websocket path: ws/driver/{driverId}
        // Extract the driverId from the WebSocket URI
        String driverId = session.getUri().getPath().split("/")[3];  // Assuming the URI is /ws/driver/{driverId}

        // Retrieve driver from database or create a new one if not found
        Driver driver = driverRepository.findById(driverId).orElseThrow(() ->new NoSuchElementException("Driver not found"));

        // Store the driverId in the session attributes
        session.getAttributes().put("driverId", driverId);
        driverSessions.put(driverId, session);

        //Store the driver object in the session attributes
        session.getAttributes().put("driver", driver);
        // Put the driver object in memory
        driverMemory.put(driverId, driver);
        // Mark the driver as online and available, e.g., by updating the database
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        // Process incoming messages, e.g., location updates or availability changes
        String driverId = (String) session.getAttributes().get("driverId");
        String payload = (String) message.getPayload();
        Driver driver = (Driver) session.getAttributes().get("driver");
        // You could deserialize the payload to a JSON object
        // driver location update
        driver.setDriverLocationFromWebSocketPayload(payload);



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
