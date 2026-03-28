package com.pharmaTrace.controller;

import com.pharmaTrace.dto.request.StatusUpdateRequest;
import com.pharmaTrace.dto.response.ApiResponse;
import com.pharmaTrace.dto.response.MedicineResponse;
import com.pharmaTrace.entity.*;
import com.pharmaTrace.repository.MedicineRepository;
import com.pharmaTrace.repository.ShipmentMedicineRepository;
import com.pharmaTrace.repository.ShipmentRepository;
import com.pharmaTrace.repository.UserRepository;
import com.pharmaTrace.service.MedicineService;
import com.pharmaTrace.service.ShipmentService;
import com.pharmaTrace.service.TrackingService;
import com.pharmaTrace.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/distributor")
@RequiredArgsConstructor
@Tag(name = "Distributor", description = "Distributor APIs")
@PreAuthorize("hasRole('DISTRIBUTOR')")
public class DistributorController {

    private final MedicineService medicineService;
    private final TrackingService trackingService;
    private final ShipmentRepository shipmentRepository;
    private final UserService userService;
    private final MedicineRepository medicineRepository;
    private final ShipmentMedicineRepository shipmentMedicineRepository;

    @PostMapping("/update-status")
    @Operation(summary = "Update medicine status during distribution")
    public ResponseEntity<ApiResponse<MedicineResponse>> updateStatus(
            @Valid @RequestBody StatusUpdateRequest statusUpdateRequest,
            Authentication authentication) {

        MedicineResponse response = medicineService.updateMedicineStatus(
                statusUpdateRequest.getMedicineId(),
                statusUpdateRequest.getStatus(),
                null
        );

        trackingService.createTrackingLog(
                statusUpdateRequest.getMedicineId(),
                statusUpdateRequest.getStatus(),
                null,
                statusUpdateRequest.getRemarks(),
                statusUpdateRequest.getLocation(),
                statusUpdateRequest.getGpsCoordinates()
        );

        return ResponseEntity.ok(ApiResponse.success("Status updated successfully", response));
    }

    @GetMapping("/medicine/{id}")
    @Operation(summary = "Get medicine details")
    public ResponseEntity<ApiResponse<MedicineResponse>> getMedicine(@PathVariable Long id) {
        MedicineResponse response = medicineService.getMedicineById(id);
        return ResponseEntity.ok(ApiResponse.success("Medicine retrieved successfully", response));
    }

    //Distributor Getting Shipment Status
    @GetMapping("/shipments")
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public ResponseEntity<ApiResponse<List<Shipment>>> getDistributorShipments(
            Authentication authentication
    ) {

        String email = authentication.getName();
        var distributor = userService.getUserEntityByEmail(email);

        List<Shipment> shipments = shipmentRepository.findByDistributorId(distributor.getId());

        return ResponseEntity.ok(
                ApiResponse.success("Distributor shipments fetched successfully", shipments)
        );
    }

    //Distributor Update Medicine Shipment status
    @PutMapping("/shipments/{shipmentId}/status")
    @PreAuthorize("hasRole('DISTRIBUTOR')")
    public ResponseEntity<ApiResponse<Shipment>> updateShipmentStatus(
            @PathVariable Long shipmentId,
            @RequestParam ShipmentStatus status
    ) {

        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));

        shipment.setStatus(status);

        Shipment updatedShipment = shipmentRepository.save(shipment);
        if (status == ShipmentStatus.DELIVERED) {

            List<ShipmentMedicine> shipmentMedicines =
                    shipmentMedicineRepository.findByShipmentId(shipmentId);

            for (ShipmentMedicine item : shipmentMedicines) {

                Medicine medicine = medicineRepository.findById(item.getMedicineId())
                        .orElseThrow(() -> new RuntimeException("Medicine not found"));

                medicine.setStatus(MedicineStatus.SOLD);
                medicineRepository.save(medicine);
            }
        }

        return ResponseEntity.ok(
                ApiResponse.success("Shipment status updated successfully", updatedShipment));



    }
}

