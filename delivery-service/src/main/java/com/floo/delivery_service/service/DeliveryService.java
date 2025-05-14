package com.floo.delivery_service.service;
import com.floo.delivery_service.dto.OrderDTO;
import com.floo.delivery_service.entity.Driver;
import com.floo.delivery_service.entity.DriverLocation;
import com.floo.delivery_service.entity.DriverStatus;
import com.floo.delivery_service.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryService {

    @Autowired
    DriverRepository driverRepository;

    public ResponseEntity<?> assignDriver(OrderDTO orderData) {
        Driver driver = findDriver(orderData);
        return new ResponseEntity<>( HttpStatus.OK);
    //get next action
    }


    //find delivery driver to the order
//    public Driver findDriver(OrderDTO orderDetails){
//        for (WebSocketSession session : DriverWebSocketHandler.driverSessions.values()) {
//            Driver driver = (Driver) session.getAttributes().get("driver");
//            if (driver != null && driver.getStatus() == DriverStatus.ONLINE) {
//                return driver;
//            }
//        }
//        return null; // No available driver
//    }
    public Driver findDriver(OrderDTO orderDetails) {
        if (orderDetails == null || orderDetails.getPickupLocation() == null) {
            System.err.println("Order details or pickup location is missing. Cannot find a driver.");
            // Consider throwing new IllegalArgumentException("Order details or pickup location cannot be null.");
            return null;
        }

        DriverLocation orderPickupLocation = orderDetails.getPickupLocation();
        Driver bestDriver = null;
        double minDistance = Double.MAX_VALUE;

        // Iterate over all active driver sessions
        // (Assuming OFFLINE drivers are already removed from driverSessions)
        for (WebSocketSession session : DriverWebSocketHandler.driverSessions.values()) {
            Driver driver = (Driver) session.getAttributes().get("driver");

            if (driver == null) {
                // This indicates an issue with session setup or cleanup.
                System.err.println("Null driver object found in session attributes for session ID: " + session.getId());
                continue;
            }

            // A driver is considered available if they are ONLINE.
            // The canFulfillOrder method can include other checks like vehicle type, c apacity, etc.
            if (driver.getStatus() == DriverStatus.ONLINE ) {
                DriverLocation driverLocation = driver.getDriverLocation(); // Assumes Driver has getCurrentLocation()

                if (driverLocation != null) {
                    double distance = calculateDistance(orderPickupLocation, driverLocation);

                    if (distance < minDistance) {
                        minDistance = distance;
                        bestDriver = driver;
                    }
                } else {
                    // Log if a driver is ONLINE but has no location data, if location is expected
                    System.out.println("Driver " + driver.getDriverId() + " is ONLINE but has no current location.");
                }
            }
        }

        if (bestDriver != null) {
            System.out.println("Found best driver: " + bestDriver.getDriverId() + " (Status: " + bestDriver.getStatus() + ") at distance: " + minDistance + " units.");
            // IMPORTANT: After selecting a driver, you should update their status
            // to indicate they are now assigned or busy with this order, e.g., DriverStatus.DELIVERY.
            // This typically involves:
            // 1. bestDriver.setStatus(DriverStatus.DELIVERY);
            // 2. Persisting this change: driverRepository.save(bestDriver);
            // 3. Potentially notifying the driver via WebSocket.
            // This logic should be handled carefully to prevent race conditions if multiple orders
            // are being assigned simultaneously.
        } else {
            System.out.println("No suitable driver (ONLINE and able to fulfill) found for the order at this time.");
        }

        return bestDriver;
    }

    /**
     * Helper method to calculate the distance between two locations.
     * Replace with your actual distance calculation logic (e.g., Haversine formula for lat/long).
     */
    private double calculateDistance(DriverLocation loc1, DriverLocation loc2) {
        if (loc1 == null || loc2 == null) {
            return Double.MAX_VALUE;
        }
        // This is a placeholder. Use a real geospatial distance calculation.
        // Example for latitude and longitude:
        // final int R = 6371; // Radius of the earth in km
        // double latDistance = Math.toRadians(loc2.getLatitude() - loc1.getLatitude());
        // double lonDistance = Math.toRadians(loc2.getLongitude() - loc1.getLongitude());
        // double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
        //         + Math.cos(Math.toRadians(loc1.getLatitude())) * Math.cos(Math.toRadians(loc2.getLatitude()))
        //         * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        // double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        // return R * c; // distance in km

        // Simplified Cartesian distance for example purposes:
//        double dx = loc1.getLongitude() - loc2.getLongitude();
//        double dy = loc1.getLatitude() - loc2.getLatitude();
//        return Math.sqrt(dx * dx + dy * dy);
        return 0.0;
    }
    public ResponseEntity<?> createDriver(Driver driver) {
        try {
            // Ensure driver location is initialized if not provided
            if (driver.getDriverLocation() == null) {
                driver.setDriverLocation(new DriverLocation(0.0, 0.0)); // Default location
            }
            // Set default status if not provided
            if (driver.getStatus() == null) {
                driver.setStatus(DriverStatus.OFFLINE);
            }
            // Set default availability if not provided
            if (driver.getAvailable() == null) {
                driver.setAvailable(false);
            }
            Driver savedDriver = driverRepository.save(driver);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDriver);
        } catch (Exception e) {
            // Log the exception for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating driver: " + e.getMessage());
        }
    }

    // update driver STATUS
    public ResponseEntity<?> updateDriverStatus(String driverId, DriverStatus newStatus) {
        Optional<Driver> driverOptional = driverRepository.findById(driverId);
        if (driverOptional.isPresent()) {
            Driver driver = driverOptional.get();
            driver.setStatus(newStatus);
            // Logic for availability based on status
            if (newStatus == DriverStatus.ONLINE) {
                driver.setAvailable(true); // Typically, a driver coming online is available
            } else if (newStatus == DriverStatus.OFFLINE || newStatus == DriverStatus.DELIVERY) {
                driver.setAvailable(false);
            }
            // For UNAVAILABLE status, availability should be explicitly managed or remain as is.
            // If newStatus is UNAVAILABLE, we might assume they are not available for new tasks.
            // else if (newStatus == DriverStatus.UNAVAILABLE) {
            //    driver.setAvailable(false);
            // }

            Driver updatedDriver = driverRepository.save(driver);
            return ResponseEntity.ok(updatedDriver);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Driver not found with ID: " + driverId);
        }
    }

    /**
     * Updates the information of an existing driver.
     * Can update name, availability. Location is typically updated via WebSocket.
     *
     * @param driverId      The ID of the driver to update.
     * @param driverDetails The driver object with updated details.
     * @return ResponseEntity with the updated driver or an error message.
     */
    public ResponseEntity<?> updateDriverInfo(String driverId, Driver driverDetails) {
        Optional<Driver> driverOptional = driverRepository.findById(driverId);
        if (driverOptional.isPresent()) {
            Driver existingDriver = driverOptional.get();
            // Update name if provided
            if (driverDetails.getName() != null && !driverDetails.getName().isEmpty()) {
                existingDriver.setName(driverDetails.getName());
            }
            // Update availability if provided
            if (driverDetails.getAvailable() != null) {
                existingDriver.setAvailable(driverDetails.getAvailable());
            }
            // Update status if provided (and valid)
            if (driverDetails.getStatus() != null) {
                existingDriver.setStatus(driverDetails.getStatus());
            }
            // Note: DriverLocation is usually updated through a different mechanism (e.g., WebSocket)
            // If you want to allow HTTP update for location too, uncomment and adapt:
            // if (driverDetails.getDriverLocation() != null) {
            //    existingDriver.setDriverLocation(driverDetails.getDriverLocation());
            // }

            Driver updatedDriver = driverRepository.save(existingDriver);
            return ResponseEntity.ok(updatedDriver);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Driver not found with ID: " + driverId);
        }
    }

    /**
     * Deletes a driver by their ID.
     *
     * @param driverId The ID of the driver to delete.
     * @return ResponseEntity indicating success or failure.
     */
    public ResponseEntity<?> deleteDriver(String driverId) {
        if (driverRepository.existsById(driverId)) {
            driverRepository.deleteById(driverId);
            return ResponseEntity.ok().body("Driver with ID: " + driverId + " deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Driver not found with ID: " + driverId);
        }
    }

    /**
     * Gets a driver by their ID.
     *
     * @param driverId The ID of the driver to retrieve.
     * @return ResponseEntity with the driver data or a not found message.
     */
    public ResponseEntity<?> getDriverById(String driverId) {
        Optional<Driver> driverOptional = driverRepository.findById(driverId);
        if (driverOptional.isPresent()) {
            return ResponseEntity.ok(driverOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Driver not found with ID: " + driverId);
        }
    }

    /**
     * Gets all drivers.
     * @return ResponseEntity with a list of all drivers.
     */
    public ResponseEntity<?> getAllDrivers() {
        try {
            List<Driver> drivers = driverRepository.findAll();
            if (drivers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No drivers found.");
            }
            return ResponseEntity.ok(drivers);
        } catch (Exception e) {
            // Log error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving drivers: " + e.getMessage());
        }
    }

}
