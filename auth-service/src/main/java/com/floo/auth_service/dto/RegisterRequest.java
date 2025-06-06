package com.floo.auth_service.dto;
import com.floo.auth_service.model.DriverProfile;
import com.floo.auth_service.model.Profile;
import com.floo.auth_service.model.RestaurantProfile;
import com.floo.auth_service.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    private String email;
    private String username;
    private String password;
    private Role role;

    // Optional profile information based on role
    private Profile profile;
    private DriverProfile driverProfile;
    private RestaurantProfile restaurantProfile;
}