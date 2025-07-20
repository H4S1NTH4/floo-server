package com.floo.auth_service.model;
//title , auther, price, quantity , availability

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Books")
public class Book {

    @Id
    private String id;

    private String title;
    private String auther;
    private Double price;
    private int quantity;
    private boolean availability;

    //deafault constructor
    public Book(){}

    public Book(String title, String auther,Double price, int quantity){
        this.title = title;
        this.auther = auther;
        this.price = price;
        this.quantity = quantity;
        if(quantity >0){
            this.availability = true;
        }else{
            this.availability = false;
        }
    }

    public String getId(){
        return id;
    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getAuther(){
        return auther;
    }
    public void setAuther(String auther){
        this.auther = auther;
    }
    public int getQuantity(){
        return quantity;
    }
    public void setQuantity(int num){
        this.quantity = num;
    }
}
