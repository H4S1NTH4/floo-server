package com.floo.auth_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverProfile {
    private String licenseNumber;
    private String vehicleType;
    private String vehicleNumber;
    private String profileImageUrl; // Optional - if different from general profile
}
