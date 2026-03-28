package com.pharmaTrace.dto.response;

import com.pharmaTrace.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;

    private String name;

    private String email;

    private Role role;

    private Boolean isActive;

    private String organizationName;

    private String phone;

    private String address;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String lastLogin;
}