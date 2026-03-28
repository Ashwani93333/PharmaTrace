package com.pharmaTrace.controller;

import com.pharmaTrace.dto.request.QRVerificationRequest;
import com.pharmaTrace.dto.response.ApiResponse;
import com.pharmaTrace.dto.response.SupplyChainHistoryResponse;
import com.pharmaTrace.dto.response.MedicineResponse;
import com.pharmaTrace.service.MedicineService;
import com.pharmaTrace.service.QRCodeService;
import com.pharmaTrace.service.TrackingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pharmacy")
@RequiredArgsConstructor
@Tag(name = "Pharmacy", description = "Pharmacy APIs")
@PreAuthorize("hasRole('PHARMACY')") // 🔥 ONLY SUPER ADMIN
public class PharmacyController {

    private final QRCodeService qrCodeService;
    private final MedicineService medicineService;
    private final TrackingService trackingService;

    @PostMapping("/verify-qr")
    @Operation(summary = "Verify medicine authenticity via QR code")
    public ResponseEntity<ApiResponse<SupplyChainHistoryResponse>> verifyQR(
            @Valid @RequestBody QRVerificationRequest verificationRequest,
            Authentication authentication) {

        String qrCode = qrCodeService.verifyQRCode(verificationRequest.getQrCode());
        MedicineResponse medicine = medicineService.getMedicineByQrCode(qrCode);

        medicineService.verifyMedicine(medicine.getId(), null);

        SupplyChainHistoryResponse response = trackingService.getSupplyChainHistory(medicine.getId());
        response.setMedicine(medicine);

        return ResponseEntity.ok(ApiResponse.success("QR code verified successfully", response));
    }

    @GetMapping("/medicine/{id}")
    @Operation(summary = "Get medicine details")
    public ResponseEntity<ApiResponse<MedicineResponse>> getMedicine(@PathVariable Long id) {
        MedicineResponse response = medicineService.getMedicineById(id);
        return ResponseEntity.ok(ApiResponse.success("Medicine retrieved successfully", response));
    }

    @GetMapping("/supply-chain/{medicineId}")
    @Operation(summary = "Get complete supply chain history")
    public ResponseEntity<ApiResponse<SupplyChainHistoryResponse>> getSupplyChainHistory(
            @PathVariable Long medicineId) {

        SupplyChainHistoryResponse response = trackingService.getSupplyChainHistory(medicineId);
        return ResponseEntity.ok(ApiResponse.success("Supply chain history retrieved successfully", response));
    }
}