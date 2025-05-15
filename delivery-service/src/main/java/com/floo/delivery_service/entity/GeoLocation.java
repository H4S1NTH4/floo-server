package com.floo.delivery_service.entity;

import org.springframework.data.mongodb.core.mapping.Field;

public class GeoLocation {
    @Field("latitude")
    private Double latitude;
    @Field("longitude")
    private Double longitude;

    public GeoLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public GeoLocation( ) {  }

    public Double getLatitude() {
        return latitude;
    }


    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
