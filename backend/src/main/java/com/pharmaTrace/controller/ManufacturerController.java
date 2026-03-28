//package com.pharmaTrace.controller;
//
//import com.google.zxing.WriterException;
//import com.pharmaTrace.dto.request.MedicineRequest;
//import com.pharmaTrace.dto.response.ApiResponse;
//import com.pharmaTrace.dto.response.MedicineResponse;
//import com.pharmaTrace.dto.response.QRCodeResponse;
//import com.pharmaTrace.dto.response.PaginationResponse;
//import com.pharmaTrace.service.MedicineService;
//import com.pharmaTrace.service.QRCodeService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//
//@RestController
//@PreAuthorize("hasRole('MANUFACTURER')")
//@RequestMapping("/api/v1/manufacturer")
//@RequiredArgsConstructor
//@Tag(name = "Manufacturer", description = "Manufacturer APIs")
//public class ManufacturerController {
//
//    private final MedicineService medicineService;
//    private final QRCodeService qrCodeService;
//
//    @PostMapping("/medicine")
//    @Operation(summary = "Create a new medicine batch")
//    public ResponseEntity<ApiResponse<MedicineResponse>> createMedicine(
//            @Valid @RequestBody MedicineRequest medicineRequest,
//            Authentication authentication) {
//
//        MedicineResponse response = medicineService.createMedicine(medicineRequest, null, authentication.getName());
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(ApiResponse.successCreated("Medicine batch created successfully", response));
//    }
//
//    @PostMapping("/generate-qr/{medicineId}")
//    @Operation(summary = "Generate QR code for medicine")
//    public ResponseEntity<ApiResponse<QRCodeResponse>> generateQRCode(
//            @PathVariable Long medicineId,
//            Authentication authentication) throws IOException, WriterException {
//
//        QRCodeResponse response = qrCodeService.generateQRCode(medicineId, null);
//        return ResponseEntity.ok(ApiResponse.success("QR code generated successfully", response));
//    }
//
//    @GetMapping("/medicines")
//    @Operation(summary = "Get all medicines created by manufacturer")
//    public ResponseEntity<ApiResponse<PaginationResponse<MedicineResponse>>> getMyMedicines(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            Authentication authentication) {
//
//        Pageable pageable = PageRequest.of(page, size);
//        Page<MedicineResponse> medicines = medicineService.getMedicinesByManufacturer(null, pageable);
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
//}

package com.pharmaTrace.controller;

import com.pharmaTrace.dto.request.MedicineRequest;
import com.pharmaTrace.dto.request.ShipmentRequest;
import com.pharmaTrace.dto.response.ApiResponse;
import com.pharmaTrace.dto.response.MedicineResponse;
import com.pharmaTrace.dto.response.PaginationResponse;
import com.pharmaTrace.dto.response.ShipmentResponse;
import com.pharmaTrace.service.MedicineService;
import com.pharmaTrace.service.ShipmentService;
import com.pharmaTrace.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/manufacturer")
@RequiredArgsConstructor
@Tag(name = "Manufacturer", description = "Manufacturer APIs")
@SecurityRequirement(name = "Bearer Authentication")
public class ManufacturerController {

    private final MedicineService medicineService;
    private final UserService userService;
    private final ShipmentService shipmentService;

    /**
     * Create medicine - FIX: Extract manufacturer ID from authenticated user
     */
    @PostMapping("/medicine")
    @PreAuthorize("hasRole('MANUFACTURER')")
    @Operation(summary = "Create medicine", description = "Create new medicine batch")
    public ResponseEntity<ApiResponse<MedicineResponse>> createMedicine(
            @Valid @RequestBody MedicineRequest medicineRequest,
            Authentication authentication) {

        log.info("Creating medicine request received");

        // ===== KEY FIX: Get manufacturer info from authentication =====
        String manufacturerEmail = authentication.getName();
        log.debug("Manufacturer email from auth: {}", manufacturerEmail);

        // Get manufacturer user details
        var manufacturerUser = userService.getUserEntityByEmail(manufacturerEmail);
        Long manufacturerId = manufacturerUser.getId();
        String manufacturerName = manufacturerUser.getName();

        log.debug("Manufacturer ID: {}, Name: {}", manufacturerId, manufacturerName);

        // Create medicine with proper manufacturer ID
        MedicineResponse response = medicineService.createMedicine(
                medicineRequest,
                manufacturerId,           // ===== PASS MANUFACTURER ID =====
                manufacturerName          // ===== PASS MANUFACTURER NAME =====
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.successCreated("Medicine created successfully", response));
    }

    /**
     * Get my medicines
     */
    @GetMapping("/medicines")
    @PreAuthorize("hasRole('MANUFACTURER')")
    @Operation(summary = "Get my medicines", description = "Get all medicines created by manufacturer")
    public ResponseEntity<ApiResponse<PaginationResponse<MedicineResponse>>> getMyMedicines(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {

        log.debug("Fetching medicines for manufacturer: {}", authentication.getName());

        String manufacturerEmail = authentication.getName();
        var manufacturerUser = userService.getUserEntityByEmail(manufacturerEmail);
        Long manufacturerId = manufacturerUser.getId();

        Pageable pageable = PageRequest.of(page, size);
        Page<MedicineResponse> medicines = medicineService.getMedicinesByManufacturer(manufacturerId, pageable);

        PaginationResponse<MedicineResponse> response = PaginationResponse.<MedicineResponse>builder()
                .content(medicines.getContent())
                .pageNumber(medicines.getNumber())
                .pageSize(medicines.getSize())
                .totalElements(medicines.getTotalElements())
                .totalPages(medicines.getTotalPages())
                .isFirst(medicines.isFirst())
                .isLast(medicines.isLast())
                .build();

        return ResponseEntity.ok(ApiResponse.success("Medicines retrieved successfully", response));
    }

    /**
     * Get medicine by ID
     */
    @GetMapping("/medicines/{medicineId}")
    @PreAuthorize("hasRole('MANUFACTURER')")
    @Operation(summary = "Get medicine by ID", description = "Get specific medicine by ID")
    public ResponseEntity<ApiResponse<MedicineResponse>> getMedicineById(@PathVariable Long medicineId) {

        log.debug("Fetching medicine: {}", medicineId);

        MedicineResponse response = medicineService.getMedicineById(medicineId);

        return ResponseEntity.ok(ApiResponse.success("Medicine retrieved successfully", response));
    }

    /**
     * Update medicine status
     */
    @PutMapping("/medicines/{medicineId}/status")
    @PreAuthorize("hasRole('MANUFACTURER')")
    @Operation(summary = "Update medicine status", description = "Update medicine status")
    public ResponseEntity<ApiResponse<MedicineResponse>> updateMedicineStatus(
            @PathVariable Long medicineId,
            @RequestParam String status,
            Authentication authentication) {

        log.info("Updating medicine status: {}", medicineId);

        String manufacturerEmail = authentication.getName();
        var manufacturerUser = userService.getUserEntityByEmail(manufacturerEmail);
        Long manufacturerId = manufacturerUser.getId();

        // Parse status string to enum
        com.pharmaTrace.entity.MedicineStatus medicineStatus =
                com.pharmaTrace.entity.MedicineStatus.valueOf(status.toUpperCase());

        MedicineResponse response = medicineService.updateMedicineStatus(medicineId, medicineStatus, manufacturerId);

        return ResponseEntity.ok(ApiResponse.success("Medicine status updated successfully", response));
    }

    /**
     * Get medicine statistics
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('MANUFACTURER')")
    @Operation(summary = "Get statistics", description = "Get medicine statistics for manufacturer")
    public ResponseEntity<ApiResponse<Object>> getStatistics(Authentication authentication) {

        log.debug("Fetching statistics for manufacturer: {}", authentication.getName());

        Object stats = medicineService.getMedicineStatistics();

        return ResponseEntity.ok(ApiResponse.success("Statistics retrieved successfully", stats));
    }

    /**
     * Get medicine by QR code
     */
    @GetMapping("/medicines/qr/{qrCode}")
    @PreAuthorize("hasRole('MANUFACTURER')")
    @Operation(summary = "Get medicine by QR code", description = "Get medicine by QR code")
    public ResponseEntity<ApiResponse<MedicineResponse>> getMedicineByQR(@PathVariable String qrCode) {

        log.debug("Fetching medicine by QR: {}", qrCode);

        MedicineResponse response = medicineService.getMedicineByQrCode(qrCode);

        return ResponseEntity.ok(ApiResponse.success("Medicine retrieved successfully", response));
    }

    // Update the status of Medicine via Shipment
    @PostMapping("/shipments")
    @PreAuthorize("hasRole('MANUFACTURER')")
    public ResponseEntity<ApiResponse<ShipmentResponse>> createShipment(
            @RequestBody ShipmentRequest request,
            Authentication authentication
    ) {

        String email = authentication.getName();
        var manufacturer = userService.getUserEntityByEmail(email);

        ShipmentResponse response = shipmentService.createShipment(
                request,
                manufacturer.getId()
        );

        return ResponseEntity.ok(
                ApiResponse.success("Shipment created successfully", response)
        );
    }
}


