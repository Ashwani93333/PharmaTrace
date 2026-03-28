package com.pharmaTrace.service;

import com.pharmaTrace.dto.response.TrackingLogResponse;
import com.pharmaTrace.dto.response.SupplyChainHistoryResponse;
import com.pharmaTrace.entity.TrackingLog;
import com.pharmaTrace.entity.Medicine;
import com.pharmaTrace.entity.MedicineStatus;
import com.pharmaTrace.entity.User;
import com.pharmaTrace.repository.TrackingLogRepository;
import com.pharmaTrace.repository.MedicineRepository;
import com.pharmaTrace.repository.UserRepository;
import com.pharmaTrace.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TrackingService {

    private final TrackingLogRepository trackingLogRepository;
    private final MedicineRepository medicineRepository;
    private final UserRepository userRepository;

    public TrackingLogResponse createTrackingLog(Long medicineId, MedicineStatus status, Long userId,
                                                 String remarks, String location, String gpsCoordinates) {
        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        TrackingLog trackingLog = TrackingLog.builder()
                .medicineId(medicineId)
                .status(status)
                .updatedBy(userId)
                .updatedByName(user.getName())
                .updatedByRole(user.getRole().name())
                .remarks(remarks)
                .location(location)
                .gpsCoordinates(gpsCoordinates)
                .qrCodeHash(medicine.getQrCode())
                .createdAt(LocalDateTime.now())
                .build();

        TrackingLog savedLog = trackingLogRepository.save(trackingLog);
        log.info("Tracking log created for medicine ID: {}", medicineId);

        return convertToTrackingLogResponse(savedLog);
    }

    public SupplyChainHistoryResponse getSupplyChainHistory(Long medicineId) {
        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found"));

        List<TrackingLog> logs = trackingLogRepository.findByMedicineIdOrderByCreatedAtAsc(medicineId);

        List<TrackingLogResponse> history = logs.stream()
                .map(this::convertToTrackingLogResponse)
                .collect(Collectors.toList());

        return SupplyChainHistoryResponse.builder()
                .isAuthentic(true)
                .verificationCount(medicine.getVerificationCount())
                .currentStatus(medicine.getDescription())
                .trackingHistory(history)
                .message("Supply chain verified successfully")
                .build();
    }

    public List<TrackingLogResponse> getTrackingLogsForMedicine(Long medicineId) {
        return trackingLogRepository.findByMedicineIdOrderByCreatedAtAsc(medicineId)
                .stream()
                .map(this::convertToTrackingLogResponse)
                .collect(Collectors.toList());
    }

    private TrackingLogResponse convertToTrackingLogResponse(TrackingLog trackingLog) {
        return TrackingLogResponse.builder()
                .id(trackingLog.getId())
                .medicineId(trackingLog.getMedicineId())
                .status(trackingLog.getStatus())
                .updatedBy(trackingLog.getUpdatedBy())
                .updatedByName(trackingLog.getUpdatedByName())
                .updatedByRole(trackingLog.getUpdatedByRole())
                .createdAt(trackingLog.getCreatedAt())
                .remarks(trackingLog.getRemarks())
                .location(trackingLog.getLocation())
                .gpsCoordinates(trackingLog.getGpsCoordinates())
                .metadata(trackingLog.getMetadata())
                .build();
    }
}