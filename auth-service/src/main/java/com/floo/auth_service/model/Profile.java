package com.floo.auth_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {
    private String fullName;
    private String phoneNumber;
    private String profileImageUrl; // URL to S3, Firebase, or static path
}
