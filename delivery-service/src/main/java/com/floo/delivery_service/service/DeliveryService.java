package com.floo.delivery_service.service;
import com.floo.delivery_service.dto.OrderDTO;
import com.floo.delivery_service.entity.Driver;
import com.floo.delivery_service.entity.DriverStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

@Service
public class DeliveryService {

    public String deliverOrder(String orderId) {
        return  "hi";}

    //assigning delivery driver to the order
    public Driver assignDeliveryDriver(OrderDTO orderDetails){
        for (WebSocketSession session : DriverWebSocketHandler.driverSessions.values()) {
            Driver driver = (Driver) session.getAttributes().get("driver");
            if (driver != null && driver.getStatus() == DriverStatus.ONLINE) {
                return driver;
            }
        }
        return null; // No available driver
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
}
