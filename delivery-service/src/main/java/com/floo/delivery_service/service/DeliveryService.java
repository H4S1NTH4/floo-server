package com.floo.delivery_service.service;
import com.floo.delivery_service.dto.OrderDTO;
import com.floo.delivery_service.entity.Driver;
import com.floo.delivery_service.entity.DriverStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import javax.xml.stream.Location;

@Service
public class DeliveryService {

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

        Location orderPickupLocation = orderDetails.getPickupLocation();
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
                Location driverLocation = driver.getLocation(); // Assumes Driver has getCurrentLocation()

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
    private double calculateDistance(Location loc1, Location loc2) {
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
}



//    private List<Driver> getAvailableDrivers() {
//
//
//    }


//    //update order status
//    public void updateOrderStatus(){
//
//    }
//
//    //notify to a customer
//    public void notifyCustomer() {
//
//    }
//}
