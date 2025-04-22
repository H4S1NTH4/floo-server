package com.floo.delivery_service.service;
import com.floo.delivery_service.dto.OrderDTO;
import com.floo.delivery_service.entity.Driver;
import org.springframework.stereotype.Service;

@Service
public class DeliveryService {

    public String deliverOrder(String orderId) {
        return  "hi";}

    //assigning delivery driver to the order
    public Driver assignDeliveryDriver(OrderDTO orderDetails){

        Driver driver = new Driver();

        return driver;
    }

    //update order status
    public void updateOrderStatus(){

    }

    //notify to a customer
    public void notifyCustomer() {

    }
}
