package com.floo.delivery_service.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "driver")
public class Driver {
    @Id
    private String orderId;
    private String name;
    private String location;


}
