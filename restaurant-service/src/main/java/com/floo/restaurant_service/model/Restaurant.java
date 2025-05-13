package com.floo.restaurant_service.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "restaurants")
public class Restaurant {
    @Id
    private String id;
    private String name;
    private String description;
    private Address address;
    private List<Long> contactInfo;
    private Double rating;
    private OwnerInfo owner;
    private boolean isAvailable = true;
    private boolean isVerified = false;

    //getters and setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Address getAddress() {
        return address;
    }
    public void setAddress(Address address) {
        this.address = address;
    }
    public List<Long> getContactInfo() {
        return contactInfo;
    }
    public void setContactInfo(List<Long> contactInfo) {
        this.contactInfo = contactInfo;
    }
    public Double getRating() {
        return rating;
    }
    public void setRating(Double rating) {
        this.rating = rating;
    }
    public OwnerInfo getOwner() {
        return owner;
    }
    public void setOwner(OwnerInfo owner) {
        this.owner = owner;
    }
    public boolean isAvailable() {
        return isAvailable;
    }
    public void setAvailable(boolean available) {
        isAvailable = available;
    }
    public boolean isVerified() {
        return isVerified;
    }
    public void setVerified(boolean verified) {
        isVerified = verified;
    }

}
