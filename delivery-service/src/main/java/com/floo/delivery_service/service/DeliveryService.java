package com.floo.delivery_service.service;
import com.floo.delivery_service.dto.OrderDTO;
import com.floo.delivery_service.entity.Driver;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryService {

    public String deliverOrder(String orderId) {
        return  "hi";}

    //assigning delivery driver to the order
//    public Driver assignDeliveryDriver(OrderDTO orderDetails){
//        // Normally, you'd fetch from a DB or a list of drivers
//        List<Driver> availableDrivers = getAvailableDrivers();
//
//        if (availableDrivers.isEmpty()) {
//            throw new RuntimeException("No available drivers at the moment.");
//        }
//
//        // Simple logic: assign the first available driver
//        Driver selectedDriver = availableDrivers.get(0);
//
//        // Update the driver's status (for example)
//        selectedDriver.setAvailable(false);
//        selectedDriver.setAssignedOrderId(orderDetails.getOrderId());
//
//        // Here, you'd persist the updated driver status to DB
//
//        return selectedDriver;
//
//    }

//    private List<Driver> getAvailableDrivers() {
//
//
//    }


    //update order status
    public void updateOrderStatus(){

    }

    //notify to a customer
    public void notifyCustomer() {

    }
}
