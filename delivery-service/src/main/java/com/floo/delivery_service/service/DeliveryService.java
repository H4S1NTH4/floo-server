package com.floo.delivery_service.service;
import com.floo.delivery_service.dto.OrderDTO;
import com.floo.delivery_service.entity.Driver;
import com.floo.delivery_service.entity.GeoLocation;
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

        if(driver==null){
            return ResponseEntity.ok("no drivers at the moment");
        }
        driver.setStatus(DriverStatus.DELIVERY);
        driverRepository.save(driver);
        return ResponseEntity.ok(driver);
    //get next action
    }

    public Driver findDriver(OrderDTO orderDetails) {
        if (orderDetails == null || orderDetails.getPickupLocation() == null) {
            System.err.println("Order details or pickup location is missing. Cannot find a driver.");
            // Consider throwing: throw new IllegalArgumentException("Order details or pickup location cannot be null.");
            return null;
        }

        GeoLocation orderPickupLocation = orderDetails.getPickupLocation();
        Driver bestDriver = null;
        double minDistance = Double.MAX_VALUE;

        // 1. Fetch ONLINE drivers from the database
        List<Driver> onlineDrivers;
        try {
            onlineDrivers = driverRepository.findByStatus(DriverStatus.ONLINE); // Assuming a method like this exists in your repository
        } catch (Exception e) {
            System.err.println("Error fetching drivers from database: " + e.getMessage());
            // Depending on the error, you might want to throw a custom exception or return null
            return null;
        }


        if (onlineDrivers.isEmpty()) {
            System.out.println("No drivers are currently ONLINE.");
            return null;
        }

        System.out.println("Found " + onlineDrivers.size() + " ONLINE drivers. Evaluating for order...");

        // 2. Iterate over the ONLINE drivers fetched from the DB
        for (Driver driver : onlineDrivers) {

            GeoLocation driverLocation = driver.getDriverLocation(); // Assumes Driver entity has getDriverLocation()
            System.out.println("Driver location: " + driverLocation + driverLocation.getLongitude() + driverLocation.getLatitude());

            if (driverLocation != null &&
                    driverLocation.getLatitude() != null &&  // Check if the Double object itself is null
                    driverLocation.getLongitude() != null) {
                double distance = calculateDistance(orderPickupLocation, driverLocation);
                System.out.println("Calculated distance: " + distance);

                System.out.println("Evaluating Driver ID: " + driver.getDriverId() + " at distance: " + distance + " units. Status: " + driver.getStatus());


                if (distance < minDistance) {
                    minDistance = distance;
                    bestDriver = driver;

                }
            } else {
                System.out.println("Driver " + driver.getDriverId() + " (Status: " + driver.getStatus() + ") is ONLINE but has no valid current location in the database.");
            }
        }

        if (bestDriver != null) {
            System.out.println("Found best driver: " + bestDriver.getDriverId() + " (Status: " + bestDriver.getStatus() + ") at distance: " + minDistance + " units.");

        } else {
            System.out.println("No suitable driver (ONLINE with location and able to fulfill) found for the order at this time.");
        }

        return bestDriver;
    }

    private double calculateDistance(GeoLocation loc1, GeoLocation loc2) {
        if (loc1 == null || loc2 == null) {
            return Double.MAX_VALUE;
        }
        // This is a placeholder. Use a real geospatial distance calculation.
        // Example for latitude and longitude:
//         final int R = 6371; // Radius of the earth in km
//         double latDistance = Math.toRadians(loc2.getLatitude() - loc1.getLatitude());
//         double lonDistance = Math.toRadians(loc2.getLongitude() - loc1.getLongitude());
//         double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
//                 + Math.cos(Math.toRadians(loc1.getLatitude())) * Math.cos(Math.toRadians(loc2.getLatitude()))
//                 * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
//         double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//         return R * c; // distance in km

         //Simplified Cartesian distance for example purposes:
        double dx = loc1.getLongitude() - loc2.getLongitude();
        double dy = loc1.getLatitude() - loc2.getLatitude();
        return Math.sqrt(dx * dx + dy * dy);
       // return 0.0;
    }
    public ResponseEntity<?> createDriver(Driver driver) {
        try {
            // Ensure driver location is initialized if not provided
            if (driver.getDriverLocation() == null) {
                driver.setDriverLocation(new GeoLocation(0.0, 0.0)); // Default location
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

    public ResponseEntity<?> deleteDriver(String driverId) {
        if (driverRepository.existsById(driverId)) {
            driverRepository.deleteById(driverId);
            return ResponseEntity.ok().body("Driver with ID: " + driverId + " deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Driver not found with ID: " + driverId);
        }
    }

    public ResponseEntity<?> getDriverById(String driverId) {
        Optional<Driver> driverOptional = driverRepository.findById(driverId);
        if (driverOptional.isPresent()) {
            return ResponseEntity.ok(driverOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Driver not found with ID: " + driverId);
        }
    }

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
    public GeoLocation getDriverLocationById(String driverId) {
        Optional<Driver> optionalDriver = driverRepository.findById(driverId);
        if (optionalDriver.isPresent()) {
            return optionalDriver.get().getDriverLocation();
        }
        return null; // or throw custom NotFoundException
    }


}



//    public Driver findDriverFromWebSocketSessions(OrderDTO orderDetails) {
//        if (orderDetails == null || orderDetails.getPickupLocation() == null) {
//            System.err.println("Order details or pickup location is missing. Cannot find a driver.");
//            // Consider throwing new IllegalArgumentException("Order details or pickup location cannot be null.");
//            return null;
//        }
//
//        GeoLocation orderPickupLocation = orderDetails.getPickupLocation();
//        Driver bestDriver = null;
//        double minDistance = Double.MAX_VALUE;
//
//        // Iterate over all active driver sessions
//        // (Assuming OFFLINE drivers are already removed from driverSessions)
//        for (WebSocketSession session : DriverWebSocketHandler.driverSessions.values()) {
//            Driver driver = (Driver) session.getAttributes().get("driver");
//
//            if (driver == null) {
//                // This indicates an issue with session setup or cleanup.
//                System.err.println("Null driver object found in session attributes for session ID: " + session.getId());
//                continue;
//            }
//
//            // A driver is considered available if they are ONLINE.
//            if (driver.getStatus() == DriverStatus.ONLINE ) {
//                GeoLocation geoLocation = driver.getDriverLocation(); // Assumes Driver has getCurrentLocation()
//
//                if (geoLocation != null) {
//                    double distance = calculateDistance(orderPickupLocation, geoLocation);
//
//                    if (distance < minDistance) {
//                        minDistance = distance;
//                        bestDriver = driver;
//                    }
//                } else {
//                    // Log if a driver is ONLINE but has no location data, if location is expected
//                    System.out.println("Driver " + driver.getDriverId() + " is ONLINE but has no current location.");
//                }
//            }
//        }
//
//        if (bestDriver != null) {
//            System.out.println("Found best driver: " + bestDriver.getDriverId() + " (Status: " + bestDriver.getStatus() + ") at distance: " + minDistance + " units.");
//            // IMPORTANT: After selecting a driver, you should update their status
//            // to indicate they are now assigned or busy with this order, e.g., DriverStatus.DELIVERY.
//            // This typically involves:
//            // 1. bestDriver.setStatus(DriverStatus.DELIVERY);
//            // 2. Persisting this change: driverRepository.save(bestDriver);
//            // 3. Potentially notifying the driver via WebSocket.
//            // This logic should be handled carefully to prevent race conditions if multiple orders
//            // are being assigned simultaneously.
//        } else {
//            System.out.println("No suitable driver (ONLINE and able to fulfill) found for the order at this time.");
//        }
//
//        return bestDriver;
//    }
