package com.pharmaTrace.service;

import com.pharmaTrace.config.JwtTokenProvider;
import com.pharmaTrace.dto.request.AuthenticationRequest;
import com.pharmaTrace.dto.request.RegisterRequest;

import com.pharmaTrace.dto.response.AuthenticationResponse;
import com.pharmaTrace.dto.response.UserResponse;
import com.pharmaTrace.entity.User;
import com.pharmaTrace.repository.UserRepository;
import com.pharmaTrace.exception.ResourceNotFoundException;
import com.pharmaTrace.util.AuditLogUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {



    private final UserRepository userRepository;
//    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogUtil auditLogUtil;




    public AuthenticationResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole())
                .organizationName(registerRequest.getOrganizationName())
                .phone(registerRequest.getPhone())
                .address(registerRequest.getAddress())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getEmail());

//        String accessToken = tokenProvider.generateAccessToken(savedUser.getEmail());
//        String refreshToken = tokenProvider.generateRefreshToken(savedUser.getEmail());

        auditLogUtil.logAudit(savedUser.getId(), "REGISTER", "User", savedUser.getId(), "User registration successful", "SUCCESS");

        return AuthenticationResponse.builder()
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(86400000L)
                .user(convertToUserResponse(savedUser))
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByEmail(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.get().getEmail())
                .password(user.get().getPassword())
                .roles(user.get().getRole().name()) // IMPORTANT
                .build();
    }


    // Add this method to UserService if it doesn't exist
    public User getUserEntityByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }


//    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
//        try {
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            authenticationRequest.getEmail(),
//                            authenticationRequest.getPassword()
//                    )
//            );
//
//            User user = userRepository.findByEmail(authenticationRequest.getEmail())
//                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//            if (!user.getIsActive()) {
//                throw new IllegalArgumentException("User account is disabled");
//            }
//
//            user.setLastLogin(LocalDateTime.now().toString());
//            userRepository.save(user);
//
//            String accessToken = tokenProvider.generateAccessToken(authentication);
//            String refreshToken = tokenProvider.generateRefreshToken(authenticationRequest.getEmail());
//
//            auditLogUtil.logAudit(user.getId(), "LOGIN", "User", user.getId(), "User login successful", "SUCCESS");
//
//            log.info("User logged in successfully: {}", user.getEmail());
//
//            return AuthenticationResponse.builder()
//                    .accessToken(accessToken)
//                    .refreshToken(refreshToken)
//                    .tokenType("Bearer")
//                    .expiresIn(86400000L)
//                    .user(convertToUserResponse(user))
//                    .build();
//
//        } catch (AuthenticationException ex) {
//            auditLogUtil.logAudit(null, "LOGIN", "User", null, "Login attempt failed", "FAILURE");
//            throw new IllegalArgumentException("Invalid email or password");
//        }
//    }


//    //NewLogin
//    public AuthenticationResponse loginAfterAuth(Authentication authentication) {
//
//        String email = authentication.getName();
//
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//        String accessToken = tokenProvider.generateAccessToken(authentication);
//        String refreshToken = tokenProvider.generateRefreshToken(email);
//
//        return AuthenticationResponse.builder()
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .tokenType("Bearer")
//                .expiresIn(86400000L)
//                .user(convertToUserResponse(user))
//                .build();
//    }

    public AuthenticationResponse refreshToken(String refreshToken) {
        if (tokenProvider.validateToken(refreshToken)) {
            String email = tokenProvider.getEmailFromJWT(refreshToken);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            String newAccessToken = tokenProvider.generateAccessToken(email);
            String newRefreshToken = tokenProvider.generateRefreshToken(email);

            return AuthenticationResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .tokenType("Bearer")
                    .expiresIn(86400000L)
                    .user(convertToUserResponse(user))
                    .build();
        }

        throw new IllegalArgumentException("Invalid refresh token");
    }

    private UserResponse convertToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .isActive(user.getIsActive())
                .organizationName(user.getOrganizationName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .lastLogin(user.getLastLogin())
                .build();
    }


    public Page<UserResponse> getAllUsers(Pageable pageable) {
        log.debug("Fetching all users paginated: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        return userRepository.findAll(pageable)
                .map(this::convertToUserResponse);
    }

    public void disableUser(Long userId) {
        // 1. Fetch user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // 2. Check if already disabled
        if (!user.getIsActive()) {
            throw new IllegalStateException("User is already disabled");
        }

        // 3. Disable user
        user.setIsActive(false);
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        // 4. Audit log
        auditLogUtil.logAudit(
                user.getId(),
                "DISABLE",
                "User",
                user.getId(),
                "User account disabled",
                "SUCCESS"
        );

        log.info("User disabled successfully: {}", user.getEmail());
    }

    public void enableUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (user.getIsActive()) {
            throw new IllegalStateException("User is already active");
        }

        user.setIsActive(true);
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        auditLogUtil.logAudit(
                user.getId(),
                "ENABLE",
                "User",
                user.getId(),
                "User account enabled",
                "SUCCESS"
        );

        log.info("User enabled successfully: {}", user.getEmail());
    }


}