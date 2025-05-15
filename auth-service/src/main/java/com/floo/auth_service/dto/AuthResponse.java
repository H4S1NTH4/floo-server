package com.floo.auth_service.dto;

import com.floo.auth_service.model.Profile;
import com.floo.auth_service.model.DriverProfile;
import com.floo.auth_service.model.RestaurantProfile;
import com.floo.auth_service.model.Role;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private UserDto user;

}
