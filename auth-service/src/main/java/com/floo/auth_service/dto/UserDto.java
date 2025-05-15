package com.floo.auth_service.dto;

import com.floo.auth_service.model.DriverProfile;
import com.floo.auth_service.model.Profile;
import com.floo.auth_service.model.RestaurantProfile;
import com.floo.auth_service.model.Role;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String id;
    private String email;
    private String username;
    private String role;
    private Profile profile;
    private DriverProfile driverProfile;
    private RestaurantProfile restaurantProfile;
}
