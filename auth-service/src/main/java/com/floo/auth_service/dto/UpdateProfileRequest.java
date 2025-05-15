package com.floo.auth_service.dto;

import com.floo.auth_service.model.DriverProfile;
import com.floo.auth_service.model.Profile;
import com.floo.auth_service.model.RestaurantProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProfileRequest {
    private String username;
    private Profile profile;
    private DriverProfile driverProfile;
    private RestaurantProfile restaurantProfile;
}
