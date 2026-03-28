//package com.pharmaTrace.controller;
//
//import com.pharmaTrace.config.JwtTokenProvider;
//import com.pharmaTrace.dto.request.AuthenticationRequest;
//import com.pharmaTrace.dto.request.RegisterRequest;
//import com.pharmaTrace.dto.response.ApiResponse;
//import com.pharmaTrace.dto.response.AuthenticationResponse;
//import com.pharmaTrace.service.AuthenticationService;
//import com.pharmaTrace.service.UserService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/v1/auth")
//@RequiredArgsConstructor
//@Tag(name = "Authentication", description = "Authentication APIs")
//public class AuthController {
//
//    private final UserService userService;
//    private final AuthenticationService authenticationService;
//    private final JwtTokenProvider tokenProvider;
//
//    @PostMapping("/register")
//    @Operation(summary = "Register a new user")
//    public ResponseEntity<ApiResponse<AuthenticationResponse>> register(@Valid @RequestBody RegisterRequest registerRequest) {
//        AuthenticationResponse response = userService.register(registerRequest);
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(ApiResponse.successCreated("User registered successfully", response));
//    }
//
//    @PostMapping("/login")
//    @Operation(summary = "Login user")
//    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
//        AuthenticationResponse response = authenticationService.login(authenticationRequest);
//        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
//    }
//
//    @PostMapping("/refresh-token")
//    @Operation(summary = "Refresh access token")
//    public ResponseEntity<ApiResponse<AuthenticationResponse>> refreshToken(
//            @RequestHeader(name = "Authorization") String refreshToken) {
//        String token = refreshToken.replace("Bearer ", "");
//        AuthenticationResponse response = userService.refreshToken(token);
//        return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", response));
//    }
//}


package com.pharmaTrace.controller;

import com.pharmaTrace.config.JwtTokenProvider;
import com.pharmaTrace.dto.request.AuthenticationRequest;
import com.pharmaTrace.dto.request.RegisterRequest;
import com.pharmaTrace.dto.response.ApiResponse;
import com.pharmaTrace.dto.response.AuthenticationResponse;
import com.pharmaTrace.dto.response.UserResponse;
import com.pharmaTrace.service.AuthenticationService;
import com.pharmaTrace.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication APIs")
public class AuthController {

    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final JwtTokenProvider tokenProvider;

    /**
     * Register a new user - NO TOKEN RETURNED
     * Only creates the user account
     */
    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Create new user account (no token returned)")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("Registration request for: {}", registerRequest.getEmail());

        // Register user WITHOUT returning tokens
        UserResponse response = userService.register(registerRequest).getUser();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.successCreated("User registered successfully. Please login to continue.", response));
    }

    /**
     * Login user - TOKEN RETURNED
     * Authenticates user and returns JWT tokens
     */
    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticate user and receive JWT tokens")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        log.info("Login request for: {}", authenticationRequest.getEmail());

        // Login user and return tokens
        AuthenticationResponse response = authenticationService.login(authenticationRequest);

        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    /**
     * Refresh access token
     */
    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh access token", description = "Generate new access token using refresh token")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> refreshToken(
            @RequestHeader(name = "Authorization") String refreshToken) {

        log.debug("Refresh token request");

        String token = refreshToken.replace("Bearer ", "");
        AuthenticationResponse response = userService.refreshToken(token);

        return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", response));
    }
}