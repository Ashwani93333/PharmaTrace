package com.pharmaTrace.controller;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.pharmaTrace.dto.response.ApiResponse;
import com.pharmaTrace.dto.response.MedicineResponse;
import com.pharmaTrace.dto.response.PaginationResponse;
import com.pharmaTrace.dto.response.UserResponse;
import com.pharmaTrace.service.UserService;
import com.pharmaTrace.service.MedicineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") // 🔥 ONLY SUPER ADMIN
@Tag(name = "Admin", description = "Admin APIs")
public class AdminController {

    private final UserService userService;
    private final MedicineService medicineService;



    @GetMapping("/users")
    @Operation(summary = "Get all users")
    public ResponseEntity<ApiResponse<PaginationResponse<UserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<UserResponse> users = userService.getAllUsers(pageable);

        PaginationResponse<UserResponse> response = PaginationResponse.<UserResponse>builder()
                .content(users.getContent())
                .pageNumber(users.getNumber())
                .pageSize(users.getSize())
                .totalElements(users.getTotalElements())
                .totalPages(users.getTotalPages())
                .isFirst(users.isFirst())
                .isLast(users.isLast())
                .build();

        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", response));
    }

    @GetMapping("/medicines")
    @Operation(summary = "Get all medicines")
    public ResponseEntity<ApiResponse<PaginationResponse<MedicineResponse>>> getAllMedicines(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<MedicineResponse> medicines = medicineService.getAllMedicines(pageable);

        PaginationResponse<MedicineResponse> response = PaginationResponse.<MedicineResponse>builder()
                .content(medicines.getContent()) // ✅ no error now
                .pageNumber(medicines.getNumber())
                .pageSize(medicines.getSize())
                .totalElements(medicines.getTotalElements())
                .totalPages(medicines.getTotalPages())
                .isFirst(medicines.isFirst())
                .isLast(medicines.isLast())
                .build();

        return ResponseEntity.ok(ApiResponse.success("Medicines retrieved successfully", response));
    }

    @PutMapping("/users/{userId}/disable")
    @Operation(summary = "Disable user account")
    public ResponseEntity<ApiResponse<String>> disableUser(@PathVariable Long userId) {
        userService.disableUser(userId);
        return ResponseEntity.ok(ApiResponse.success("User disabled successfully", null));
    }
}

//package com.pharmaTrace.controller;
//
//import com.pharmaTrace.dto.response.ApiResponse;
//import com.pharmaTrace.dto.response.MedicineResponse;
//import com.pharmaTrace.dto.response.PaginationResponse;
//import com.pharmaTrace.dto.response.UserResponse;
//import com.pharmaTrace.service.UserService;
//import com.pharmaTrace.service.MedicineService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//
//@Slf4j
//@RestController
//@RequestMapping("/api/v1/admin")
//@RequiredArgsConstructor
//@Tag(name = "Admin", description = "Admin Management APIs")
//@SecurityRequirement(name = "Bearer Authentication")
//public class AdminController {
//
//    private final UserService userService;
//    private final MedicineService medicineService;
//
//    /**
//     * Get all users with pagination
//     */
//    @GetMapping("/users")
//    @PreAuthorize("hasRole('ADMIN')")
//    @Operation(summary = "Get all users", description = "Retrieve all users with pagination")
//    public ResponseEntity<ApiResponse<PaginationResponse<UserResponse>>> getAllUsers(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "createdAt") String sortBy,
//            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
//
//        log.debug("Fetching all users - page: {}, size: {}", page, size);
//
//        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
//        Page<UserResponse> users = userService.getAllUsers(pageable);
//
//        PaginationResponse<UserResponse> response = PaginationResponse.<UserResponse>builder()
//                .content(users.getContent())
//                .pageNumber(users.getNumber())
//                .pageSize(users.getSize())
//                .totalElements(users.getTotalElements())
//                .totalPages(users.getTotalPages())
//                .isFirst(users.isFirst())
//                .isLast(users.isLast())
//                .build();
//
//        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", response));
//    }
//
//    /**
//     * Get all active users with pagination
//     */
//    @GetMapping("/users/active")
//    @PreAuthorize("hasRole('ADMIN')")
//    @Operation(summary = "Get all active users", description = "Retrieve all active users with pagination")
//    public ResponseEntity<ApiResponse<PaginationResponse<UserResponse>>> getAllActiveUsers(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//
//        log.debug("Fetching all active users");
//
//        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
//        Page<UserResponse> users = userService.getAllActiveUsersPaginated(pageable);
//
//        PaginationResponse<UserResponse> response = PaginationResponse.<UserResponse>builder()
//                .content(users.getContent())
//                .pageNumber(users.getNumber())
//                .pageSize(users.getSize())
//                .totalElements(users.getTotalElements())
//                .totalPages(users.getTotalPages())
//                .isFirst(users.isFirst())
//                .isLast(users.isLast())
//                .build();
//
//        return ResponseEntity.ok(ApiResponse.success("Active users retrieved successfully", response));
//    }
//
//    /**
//     * Get all inactive users with pagination
//     */
//    @GetMapping("/users/inactive")
//    @PreAuthorize("hasRole('ADMIN')")
//    @Operation(summary = "Get all inactive users", description = "Retrieve all inactive users with pagination")
//    public ResponseEntity<ApiResponse<PaginationResponse<UserResponse>>> getAllInactiveUsers(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//
//        log.debug("Fetching all inactive users");
//
//        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
//        Page<UserResponse> users = userService.getAllInactiveUsersPaginated(pageable);
//
//        PaginationResponse<UserResponse> response = PaginationResponse.<UserResponse>builder()
//                .content(users.getContent())
//                .pageNumber(users.getNumber())
//                .pageSize(users.getSize())
//                .totalElements(users.getTotalElements())
//                .totalPages(users.getTotalPages())
//                .isFirst(users.isFirst())
//                .isLast(users.isLast())
//                .build();
//
//        return ResponseEntity.ok(ApiResponse.success("Inactive users retrieved successfully", response));
//    }
//
//    /**
//     * Get all medicines with pagination
//     */
//    @GetMapping("/medicines")
//    @PreAuthorize("hasRole('ADMIN')")
//    @Operation(summary = "Get all medicines", description = "Retrieve all medicines with pagination")
//    public ResponseEntity<ApiResponse<PaginationResponse<MedicineResponse>>> getAllMedicines(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "createdAt") String sortBy,
//            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
//
//        log.debug("Fetching all medicines - page: {}, size: {}", page, size);
//
//        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
//        Page<MedicineResponse> medicines = medicineService.getAllMedicinesPaginated(pageable);
//
//        PaginationResponse<MedicineResponse> response = PaginationResponse.<MedicineResponse>builder()
//                .content(medicines.getContent())
//                .pageNumber(medicines.getNumber())
//                .pageSize(medicines.getSize())
//                .totalElements(medicines.getTotalElements())
//                .totalPages(medicines.getTotalPages())
//                .isFirst(medicines.isFirst())
//                .isLast(medicines.isLast())
//                .build();
//
//        return ResponseEntity.ok(ApiResponse.success("Medicines retrieved successfully", response));
//    }
//
//    /**
//     * Get user by ID
//     */
//    @GetMapping("/users/{userId}")
//    @PreAuthorize("hasRole('ADMIN')")
//    @Operation(summary = "Get user by ID", description = "Retrieve a specific user by ID")
//    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long userId) {
//
//        log.debug("Fetching user: {}", userId);
//
//        UserResponse response = userService.getUserById(userId);
//        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", response));
//    }
//
//    /**
//     * Disable user account
//     */
//    @PutMapping("/users/{userId}/disable")
//    @PreAuthorize("hasRole('ADMIN')")
//    @Operation(summary = "Disable user account", description = "Disable a user account")
//    public ResponseEntity<ApiResponse<UserResponse>> disableUser(
//            @PathVariable Long userId,
//            @RequestParam(required = false) String reason,
//            Authentication authentication) {
//
//        log.info("Disabling user: {}", userId);
//
//        Long adminId = extractUserIdFromToken(authentication);
//        UserResponse response = userService.disableUser(userId, adminId, reason != null ? reason : "Admin disabled user");
//
//        return ResponseEntity.ok(ApiResponse.success("User disabled successfully", response));
//    }
//
//    /**
//     * Enable user account
//     */
//    @PutMapping("/users/{userId}/enable")
//    @PreAuthorize("hasRole('ADMIN')")
//    @Operation(summary = "Enable user account", description = "Enable a user account")
//    public ResponseEntity<ApiResponse<UserResponse>> enableUser(
//            @PathVariable Long userId,
//            Authentication authentication) {
//
//        log.info("Enabling user: {}", userId);
//
//        Long adminId = extractUserIdFromToken(authentication);
//        UserResponse response = userService.enableUser(userId, adminId);
//
//        return ResponseEntity.ok(ApiResponse.success("User enabled successfully", response));
//    }
//
//    /**
//     * Toggle user status
//     */
//    @PutMapping("/users/{userId}/toggle-status")
//    @PreAuthorize("hasRole('ADMIN')")
//    @Operation(summary = "Toggle user status", description = "Toggle user active/inactive status")
//    public ResponseEntity<ApiResponse<UserResponse>> toggleUserStatus(
//            @PathVariable Long userId,
//            Authentication authentication) {
//
//        log.info("Toggling user status: {}", userId);
//
//        Long adminId = extractUserIdFromToken(authentication);
//        UserResponse response = userService.toggleUserStatus(userId, adminId);
//
//        return ResponseEntity.ok(ApiResponse.success("User status toggled successfully", response));
//    }
//
//    /**
//     * Get user statistics
//     */
//    @GetMapping("/statistics/users")
//    @PreAuthorize("hasRole('ADMIN')")
//    @Operation(summary = "Get user statistics", description = "Retrieve user statistics and counts")
//    public ResponseEntity<ApiResponse<Object>> getUserStatistics() {
//
//        log.debug("Fetching user statistics");
//
//        Object stats = userService.getUserStatistics();
//        return ResponseEntity.ok(ApiResponse.success("User statistics retrieved successfully", stats));
//    }
//
//    /**
//     * Get medicine statistics
//     */
//    @GetMapping("/statistics/medicines")
//    @PreAuthorize("hasRole('ADMIN')")
//    @Operation(summary = "Get medicine statistics", description = "Retrieve medicine statistics and counts")
//    public ResponseEntity<ApiResponse<Object>> getMedicineStatistics() {
//
//        log.debug("Fetching medicine statistics");
//
//        Object stats = medicineService.getMedicineStatistics();
//        return ResponseEntity.ok(ApiResponse.success("Medicine statistics retrieved successfully", stats));
//    }
//
//    /**
//     * Search users by name
//     */
//    @GetMapping("/users/search")
//    @PreAuthorize("hasRole('ADMIN')")
//    @Operation(summary = "Search users by name", description = "Search users by name with pagination")
//    public ResponseEntity<ApiResponse<PaginationResponse<UserResponse>>> searchUsers(
//            @RequestParam String name,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//
//        log.debug("Searching users with name: {}", name);
//
//        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
//        Page<UserResponse> users = userService.searchUsersByNamePaginated(name, pageable);
//
//        PaginationResponse<UserResponse> response = PaginationResponse.<UserResponse>builder()
//                .content(users.getContent())
//                .pageNumber(users.getNumber())
//                .pageSize(users.getSize())
//                .totalElements(users.getTotalElements())
//                .totalPages(users.getTotalPages())
//                .isFirst(users.isFirst())
//                .isLast(users.isLast())
//                .build();
//
//        return ResponseEntity.ok(ApiResponse.success("Search results retrieved successfully", response));
//    }
//
//    /**
//     * Search medicines by name
//     */
//    @GetMapping("/medicines/search")
//    @PreAuthorize("hasRole('ADMIN')")
//    @Operation(summary = "Search medicines by name", description = "Search medicines by name with pagination")
//    public ResponseEntity<ApiResponse<PaginationResponse<MedicineResponse>>> searchMedicines(
//            @RequestParam String name,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//
//        log.debug("Searching medicines with name: {}", name);
//
//        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
//        Page<MedicineResponse> medicines = medicineService.searchMedicines(name, pageable);
//
//        PaginationResponse<MedicineResponse> response = PaginationResponse.<MedicineResponse>builder()
//                .content(medicines.getContent())
//                .pageNumber(medicines.getNumber())
//                .pageSize(medicines.getSize())
//                .totalElements(medicines.getTotalElements())
//                .totalPages(medicines.getTotalPages())
//                .isFirst(medicines.isFirst())
//                .isLast(medicines.isLast())
//                .build();
//
//        return ResponseEntity.ok(ApiResponse.success("Search results retrieved successfully", response));
//    }
//
//    /**
//     * Get medicine by ID
//     */
//    @GetMapping("/medicines/{medicineId}")
//    @PreAuthorize("hasRole('ADMIN')")
//    @Operation(summary = "Get medicine by ID", description = "Retrieve a specific medicine by ID")
//    public ResponseEntity<ApiResponse<MedicineResponse>> getMedicineById(@PathVariable Long medicineId) {
//
//        log.debug("Fetching medicine: {}", medicineId);
//
//        MedicineResponse response = medicineService.getMedicineById(medicineId);
//        return ResponseEntity.ok(ApiResponse.success("Medicine retrieved successfully", response));
//    }
//
//    /**
//     * Helper method to extract user ID from authentication token
//     */
//    private Long extractUserIdFromToken(Authentication authentication, Pageable pageable) {
//        try {
//            if (authentication != null && authentication.getPrincipal() != null) {
//                // Extract from JWT token or principal
//                Object principal = authentication.getPrincipal();
//
//                if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
//                    // Get username and fetch user ID from database
//                    String username = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
//                    return userService.getAllUsers(pageable);
//                }
//            }
//        } catch (Exception ex) {
//            log.error("Error extracting user ID from token", ex);
//        }
//        return null;
//    }
//}