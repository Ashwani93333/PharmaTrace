package com.pharmaTrace.dto.response;

import com.pharmaTrace.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationResponse {

    private String accessToken;

    private String refreshToken;

    private String tokenType = "Bearer";

    private Long expiresIn;

    private UserResponse user;
}