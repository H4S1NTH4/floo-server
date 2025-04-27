package com.floo.delivery_service.entity;

import javax.xml.stream.Location;

public class DriverInfo {
    private DriverStatus status;  // Using enum for status
    private Location location;    // Driver's location

    public DriverInfo(DriverStatus status, Location location) {
        this.status = status;
        this.location = location;
    }

    // Getters and setters
    public DriverStatus getStatus() {
        return status;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
