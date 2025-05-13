package com.floo.restaurant_service.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OwnerInfo {
    private String owner_id;
    private String username;
    private String fullName;
    private String phoneNumber;
}
