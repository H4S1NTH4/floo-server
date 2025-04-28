package com.floo.delivery_service.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.floo.delivery_service.dto.DriverWsPayload;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "driver")
public class Driver {
    @Id
    private String driverId;
    private String name;
//    private String location;
    private DriverStatus status;
    private Boolean available;
    private DriverLocation driverLocation;

    public Driver(String name, DriverLocation driverLocation, DriverStatus status) {
        this.name = name;
        this.driverLocation = driverLocation;
        this.status = status;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String orderId) {
        this.driverId = orderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DriverLocation getDriverLocation() {
        return driverLocation;
    }

    public void setDriverLocationFromWebSocketPayload(String payload) {

        try {
            // Use Jackson to parse the JSON payload into latitude and longitude
            ObjectMapper objectMapper = new ObjectMapper();
            DriverWsPayload driverWsPayload = objectMapper.readValue(payload, DriverWsPayload.class);

            this.driverLocation.setLatitude(driverWsPayload.getLatitude());
            this.driverLocation.setLongitude(driverWsPayload.getLongitude());
            // Convert the status string into the DriverStatus enum
            this.status = DriverStatus.valueOf(driverWsPayload.getStatus().toUpperCase());  // Ensure case-insensitivity if needed

        } catch (Exception e) {
            e.printStackTrace();
            // Handle parsing errors
        }
    }

    public DriverStatus getStatus() {
        return status;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}

