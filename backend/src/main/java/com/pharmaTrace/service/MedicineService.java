////package com.pharmaTrace.service;
////
//
//
////working
//
//
////import com.pharmaTrace.dto.request.MedicineRequest;
////import com.pharmaTrace.dto.response.MedicineResponse;
////import com.pharmaTrace.entity.Medicine;
////import com.pharmaTrace.entity.MedicineStatus;
////import com.pharmaTrace.repository.MedicineRepository;
////import com.pharmaTrace.exception.ResourceNotFoundException;
////import com.pharmaTrace.util.AuditLogUtil;
////import lombok.RequiredArgsConstructor;
////import lombok.extern.slf4j.Slf4j;
////import org.springframework.data.domain.Page;
////import org.springframework.data.domain.Pageable;
////import org.springframework.stereotype.Service;
////import org.springframework.transaction.annotation.Transactional;
////
////import java.math.BigDecimal;
////import java.time.LocalDateTime;
////import java.util.List;
////import java.util.UUID;
////
////@Slf4j
////@Service
////@RequiredArgsConstructor
////@Transactional
////public class MedicineService {
////
////    private final MedicineRepository medicineRepository;
////    private final AuditLogUtil auditLogUtil;
////
////    public MedicineResponse createMedicine(MedicineRequest medicineRequest, Long manufacturerId, String manufacturerName) {
////        String uniqueHash = UUID.randomUUID().toString();
////
////        Medicine medicine = Medicine.builder()
////                .name(medicineRequest.getName())
////                .description(medicineRequest.getDescription())
////                .batchNumber(medicineRequest.getBatchNumber())
////                .manufacturerId(manufacturerId)
////                .manufacturer(manufacturerName)
////                .serialNumber(medicineRequest.getSerialNumber())
////                .expiryDate(medicineRequest.getExpiryDate())
////                .manufacturingDate(medicineRequest.getManufacturingDate())
////                .quantity(medicineRequest.getQuantity())
////                .storageConditions(medicineRequest.getStorageConditions())
////                .qrCode(uniqueHash)
////                .status(MedicineStatus.CREATED
////                )
////                .isVerified(false)
////                .verificationCount(0)
////                .createdAt(LocalDateTime.now())
////                .updatedAt(LocalDateTime.now())
////                .build();
////
////        Medicine savedMedicine = medicineRepository.save(medicine);
////        log.info("Medicine created successfully with ID: {}", savedMedicine.getId());
////
////        auditLogUtil.logAudit(manufacturerId, "CREATE_MEDICINE", "Medicine", savedMedicine.getId(),
////                "Medicine batch created: " + savedMedicine.getName(), "SUCCESS");
////
////        return convertToMedicineResponse(savedMedicine);
////    }
////
////    public MedicineResponse getMedicineById(Long medicineId) {
////        Medicine medicine = medicineRepository.findById(medicineId)
////                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found"));
////        return convertToMedicineResponse(medicine);
////    }
////
////    public MedicineResponse getMedicineByQrCode(String qrCode) {
////        Medicine medicine = medicineRepository.findByQrCode(qrCode)
////                .orElseThrow(() -> new ResourceNotFoundException("Invalid QR code"));
////        return convertToMedicineResponse(medicine);
////    }
////
////    public Page<MedicineResponse> getMedicinesByManufacturer(Long manufacturerId, Pageable pageable) {
////        return medicineRepository.findByManufacturerId(manufacturerId, pageable)
////                .map(this::convertToMedicineResponse);
////    }
////
////    public Page<MedicineResponse> getMedicinesByStatus(MedicineStatus status, Pageable pageable) {
////        return medicineRepository.findByStatus(status, pageable)
////                .map(this::convertToMedicineResponse);
////    }
////
////    public Page<MedicineResponse> searchMedicines(String name, Pageable pageable) {
////        return medicineRepository.findByNameContainingIgnoreCase(name, pageable)
////                .map(this::convertToMedicineResponse);
////    }
////
////    public MedicineResponse updateMedicineStatus(Long medicineId, MedicineStatus newStatus, Long userId) {
////        Medicine medicine = medicineRepository.findById(medicineId)
////                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found"));
////
////        MedicineStatus oldStatus = medicine.getStatus();
////        medicine.setStatus(newStatus);
////
////        medicine.setUpdatedAt(LocalDateTime.now());
////
////        Medicine updatedMedicine = medicineRepository.save(medicine);
////        log.info("Medicine status updated from {} to {} for ID: {}", oldStatus, newStatus, medicineId);
////
////        auditLogUtil.logAudit(userId, "UPDATE_STATUS", "Medicine", medicineId,
////                "Status updated from " + oldStatus + " to " + newStatus, "SUCCESS");
////
////        return convertToMedicineResponse(updatedMedicine);
////    }
////
////    public void verifyMedicine(Long medicineId, Long pharmacyId) {
////        Medicine medicine = medicineRepository.findById(medicineId)
////                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found"));
////
////        medicine.setIsVerified(true);
////        medicine.setVerificationCount(medicine.getVerificationCount() + 1);
////        medicine.setLastVerifiedAt(LocalDateTime.now());
////        medicineRepository.save(medicine);
////
////        auditLogUtil.logAudit(pharmacyId, "VERIFY_MEDICINE", "Medicine", medicineId,
////                "Medicine verified by pharmacy", "SUCCESS");
////    }
////
////    public Page<MedicineResponse> getAllMedicines(Pageable pageable) {
////        return medicineRepository.findAll(pageable)
////                .map(this::convertToMedicineResponse);
////    }
////
////    private MedicineResponse convertToMedicineResponse(Medicine medicine) {
////        return MedicineResponse.builder()
////                .id(medicine.getId())
////                .name(medicine.getName())
////                .description(medicine.getDescription())
////                .batchNumber(medicine.getBatchNumber())
////                .manufacturerId(medicine.getManufacturerId())
////                .manufacturer(medicine.getManufacturer())
////                .serialNumber(medicine.getSerialNumber())
////                .expiryDate(medicine.getExpiryDate())
////                .manufacturingDate(medicine.getManufacturingDate())
////                .quantity(medicine.getQuantity())
////                .storageConditions(medicine.getStorageConditions())
////                .status(medicine.getStatus())
////                .price(medicine.getPrice() != null ? medicine.getPrice() : BigDecimal.ZERO)  // Default to 0 if not provided
////                .qrCode(medicine.getQrCode())
////                .isVerified(medicine.getIsVerified())
////                .verificationCount(medicine.getVerificationCount())
////                .lastVerifiedAt(medicine.getLastVerifiedAt())
////                .createdAt(medicine.getCreatedAt())
////                .updatedAt(medicine.getUpdatedAt())
////                .build();
////    }
////
////
////}
//
//
////package com.pharmaTrace.service;
////
////import com.pharmaTrace.dto.request.MedicineRequest;
////import com.pharmaTrace.dto.response.MedicineResponse;
////import com.pharmaTrace.entity.Medicine;
////import com.pharmaTrace.entity.MedicineStatus;
////import com.pharmaTrace.entity.User;
////import com.pharmaTrace.repository.MedicineRepository;
////import com.pharmaTrace.repository.UserRepository;
////import com.pharmaTrace.exception.ResourceNotFoundException;
////import com.pharmaTrace.util.AuditLogUtil;
////import lombok.RequiredArgsConstructor;
////import lombok.extern.slf4j.Slf4j;
////import org.springframework.data.domain.Page;
////import org.springframework.data.domain.Pageable;
////import org.springframework.stereotype.Service;
////import org.springframework.transaction.annotation.Transactional;
////
////import java.time.LocalDateTime;
////import java.util.List;
////import java.util.Optional;
////import java.util.UUID;
////import java.util.stream.Collectors;
////
////@Slf4j
////@Service
////@RequiredArgsConstructor
////@Transactional
////public class MedicineService {
////
////    private final MedicineRepository medicineRepository;
////    private final UserRepository userRepository;
////    private final AuditLogUtil auditLogUtil;
////
////    /**
////     * Create a new medicine batch
////     */
////    public MedicineResponse createMedicine(MedicineRequest medicineRequest, Long manufacturerId, String manufacturerName) {
////        // Verify manufacturer exists
////        User manufacturer = userRepository.findById(manufacturerId)
////                .orElseThrow(() -> new ResourceNotFoundException("Manufacturer not found"));
////
////        // Generate unique QR code
////        String uniqueHash = generateUniqueQRCode();
////
////        // Check if batch already exists for this manufacturer
////        Optional<Medicine> existingMedicine = medicineRepository
////                .findByBatchNumberAndManufacturerId(medicineRequest.getBatchNumber(), manufacturerId);
////        if (existingMedicine.isPresent()) {
////            throw new IllegalArgumentException("Medicine batch already exists for this manufacturer");
////        }
////
////        Medicine medicine = Medicine.builder()
////                .name(medicineRequest.getName())
////                .description(medicineRequest.getDescription())
////                .batchNumber(medicineRequest.getBatchNumber())
////                .manufacturer(manufacturer)
////                .quantity(medicineRequest.getQuantity())
////                .qrCode(uniqueHash)
////                .status(MedicineStatus.CREATED)
////                .createdAt(LocalDateTime.now())
////                .updatedAt(LocalDateTime.now())
////                .build();
////
////        Medicine savedMedicine = medicineRepository.save(medicine);
////        log.info("Medicine created successfully with ID: {}, QRCode: {}", savedMedicine.getId(), uniqueHash);
////
////        auditLogUtil.logCreate(manufacturerId, manufacturer.getName(), "Medicine", savedMedicine.getId(),
////                savedMedicine.getName(), "Medicine batch created with QR code: " + uniqueHash);
////
////        return convertToMedicineResponse(savedMedicine);
////    }
////
////    /**
////     * Get medicine by ID
////     */
////    public MedicineResponse getMedicineById(Long medicineId) {
////        Medicine medicine = medicineRepository.findById(medicineId)
////                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with ID: " + medicineId));
////        return convertToMedicineResponse(medicine);
////    }
////
////    /**
////     * Get medicine entity by ID (for internal use)
////     */
////    public Medicine getMedicineEntityById(Long medicineId) {
////        return medicineRepository.findById(medicineId)
////                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with ID: " + medicineId));
////    }
////
////    /**
////     * Get medicine by QR code
////     */
////    public MedicineResponse getMedicineByQrCode(String qrCode) {
////        Medicine medicine = medicineRepository.findByQrCode(qrCode)
////                .orElseThrow(() -> new ResourceNotFoundException("Invalid QR code: " + qrCode));
////        return convertToMedicineResponse(medicine);
////    }
////
////    /**
////     * Get medicine entity by QR code (for internal use)
////     */
////    public Medicine getMedicineEntityByQrCode(String qrCode) {
////        return medicineRepository.findByQrCode(qrCode)
////                .orElseThrow(() -> new ResourceNotFoundException("Invalid QR code: " + qrCode));
////    }
////
////    /**
////     * Get medicine by batch number
////     */
////    public MedicineResponse getMedicineByBatchNumber(String batchNumber) {
////        Medicine medicine = medicineRepository.findByBatchNumber(batchNumber)
////                .orElseThrow(() -> new ResourceNotFoundException("Medicine batch not found: " + batchNumber));
////        return convertToMedicineResponse(medicine);
////    }
////
////    /**
////     * Get medicine by batch number and manufacturer
////     */
////    public MedicineResponse getMedicineByBatchNumberAndManufacturer(String batchNumber, Long manufacturerId) {
////        Medicine medicine = medicineRepository.findByBatchNumberAndManufacturerId(batchNumber, manufacturerId)
////                .orElseThrow(() -> new ResourceNotFoundException("Medicine batch not found for this manufacturer"));
////        return convertToMedicineResponse(medicine);
////    }
////
////    /**
////     * Get medicines by manufacturer paginated
////     */
////    public Page<MedicineResponse> getMedicinesByManufacturer(Long manufacturerId, Pageable pageable) {
////        return medicineRepository.findByManufacturerId(manufacturerId, pageable)
////                .map(this::convertToMedicineResponse);
////    }
////
////    /**
////     * Get medicines by manufacturer
////     */
////    public List<MedicineResponse> getMedicinesByManufacturerList(Long manufacturerId) {
////        return medicineRepository.findByManufacturerId(manufacturerId)
////                .stream()
////                .map(this::convertToMedicineResponse)
////                .collect(Collectors.toList());
////    }
////
////    /**
////     * Get medicines by status paginated
////     */
////    public Page<MedicineResponse> getMedicinesByStatus(MedicineStatus status, Pageable pageable) {
////        return medicineRepository.findByStatus(status, pageable)
////                .map(this::convertToMedicineResponse);
////    }
////
////    /**
////     * Get medicines by status
////     */
////    public List<MedicineResponse> getMedicinesByStatusList(MedicineStatus status) {
////        return medicineRepository.findByStatus(status)
////                .stream()
////                .map(this::convertToMedicineResponse)
////                .collect(Collectors.toList());
////    }
////
////    /**
////     * Search medicines by name paginated
////     */
////    public Page<MedicineResponse> searchMedicines(String name, Pageable pageable) {
////        return medicineRepository.findByNameContainingIgnoreCase(name, pageable)
////                .map(this::convertToMedicineResponse);
////    }
////
////    /**
////     * Search medicines by name
////     */
////    public List<MedicineResponse> searchMedicinesList(String name) {
////        return medicineRepository.findByNameContainingIgnoreCase(name)
////                .stream()
////                .map(this::convertToMedicineResponse)
////                .collect(Collectors.toList());
////    }
////
////    /**
////     * Update medicine status
////     */
////    public MedicineResponse updateMedicineStatus(Long medicineId, MedicineStatus newStatus, Long userId) {
////        Medicine medicine = medicineRepository.findById(medicineId)
////                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found"));
////
////        MedicineStatus oldStatus = medicine.getStatus();
////        medicine.setStatus(newStatus);
////        medicine.setUpdatedAt(LocalDateTime.now());
////
////        Medicine updatedMedicine = medicineRepository.save(medicine);
////        log.info("Medicine status updated from {} to {} for ID: {}", oldStatus, newStatus, medicineId);
////
////        auditLogUtil.logStatusChange(userId, getUserName(userId), "Medicine", medicineId,
////                medicine.getName(), oldStatus.toString(), newStatus.toString());
////
////        return convertToMedicineResponse(updatedMedicine);
////    }
////
////    /**
////     * Update medicine quantity
////     */
////    public MedicineResponse updateMedicineQuantity(Long medicineId, Integer newQuantity, Long userId) {
////        Medicine medicine = medicineRepository.findById(medicineId)
////                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found"));
////
////        Integer oldQuantity = medicine.getQuantity();
////        medicine.setQuantity(newQuantity);
////        medicine.setUpdatedAt(LocalDateTime.now());
////
////        Medicine updatedMedicine = medicineRepository.save(medicine);
////        log.info("Medicine quantity updated from {} to {} for ID: {}", oldQuantity, newQuantity, medicineId);
////
////        auditLogUtil.logAuditWithChanges(userId, getUserName(userId), "UPDATE_QUANTITY",
////                "Medicine", medicineId, oldQuantity.toString(), newQuantity.toString(),
////                "Quantity updated from " + oldQuantity + " to " + newQuantity, "SUCCESS");
////
////        return convertToMedicineResponse(updatedMedicine);
////    }
////
////    /**
////     * Update medicine details
////     */
////    public MedicineResponse updateMedicineDetails(Long medicineId, MedicineRequest medicineRequest, Long userId) {
////        Medicine medicine = medicineRepository.findById(medicineId)
////                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found"));
////
////        String oldName = medicine.getName();
////        String oldDescription = medicine.getDescription();
////        Integer oldQuantity = medicine.getQuantity();
////
////        medicine.setName(medicineRequest.getName());
////        medicine.setDescription(medicineRequest.getDescription());
////        medicine.setQuantity(medicineRequest.getQuantity());
////        medicine.setUpdatedAt(LocalDateTime.now());
////
////        Medicine updatedMedicine = medicineRepository.save(medicine);
////        log.info("Medicine details updated for ID: {}", medicineId);
////
////        auditLogUtil.logUpdate(userId, getUserName(userId), "Medicine", medicineId,
////                medicine.getName(),
////                "Name: " + oldName + ", Description: " + oldDescription + ", Qty: " + oldQuantity,
////                "Name: " + medicine.getName() + ", Description: " + medicine.getDescription() + ", Qty: " + medicine.getQuantity(),
////                "Medicine details updated");
////
////        return convertToMedicineResponse(updatedMedicine);
////    }
////
////    /**
////     * Verify medicine authenticity via QR code
////     */
////    public MedicineResponse verifyMedicine(Long medicineId, Long pharmacyId) {
////        Medicine medicine = medicineRepository.findById(medicineId)
////                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found"));
////
////        User pharmacy = userRepository.findById(pharmacyId)
////                .orElseThrow(() -> new ResourceNotFoundException("Pharmacy not found"));
////
////        medicine.setUpdatedAt(LocalDateTime.now());
////        Medicine updatedMedicine = medicineRepository.save(medicine);
////
////        auditLogUtil.logMedicineVerification(pharmacyId, pharmacy.getName(), medicineId,
////                medicine.getName(), true, "Pharmacy");
////
////        log.info("Medicine verified successfully: ID={}, QRCode={}", medicineId, medicine.getQrCode());
////
////        return convertToMedicineResponse(updatedMedicine);
////    }
////
////    /**
////     * Get all medicines paginated
////     */
////    public Page<MedicineResponse> getAllMedicinesPaginated(Pageable pageable) {
////        return medicineRepository.findAll(pageable)
////                .map(this::convertToMedicineResponse);
////    }
////
////    /**
////     * Get all medicines
////     */
////    public List<MedicineResponse> getAllMedicines() {
////        return medicineRepository.findAll()
////                .stream()
////                .map(this::convertToMedicineResponse)
////                .collect(Collectors.toList());
////    }
////
////    /**
////     * Get total medicines count
////     */
////    public Long getTotalMedicinesCount() {
////        return medicineRepository.count();
////    }
////
////    /**
////     * Get medicines count by status
////     */
////    public Long getMedicinesCountByStatus(MedicineStatus status) {
////        return medicineRepository.countByStatus(status);
////    }
////
////    /**
////     * Get medicines count by manufacturer
////     */
////    public Long getMedicinesCountByManufacturer(Long manufacturerId) {
////        return medicineRepository.countByManufacturerId(manufacturerId);
////    }
////
////    /**
////     * Check if QR code exists
////     */
////    public boolean isQrCodeExists(String qrCode) {
////        return medicineRepository.findByQrCode(qrCode).isPresent();
////    }
////
////    /**
////     * Check if batch already exists for manufacturer
////     */
////    public boolean isBatchExistsForManufacturer(String batchNumber, Long manufacturerId) {
////        return medicineRepository.findByBatchNumberAndManufacturerId(batchNumber, manufacturerId).isPresent();
////    }
////
////    /**
////     * Recall medicine (mark as RECALLED)
////     */
////    public MedicineResponse recallMedicine(Long medicineId, Long userId, String reason) {
////        Medicine medicine = medicineRepository.findById(medicineId)
////                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found"));
////
////        MedicineStatus oldStatus = medicine.getStatus();
////        medicine.setStatus(MedicineStatus.RECALLED);
////        medicine.setUpdatedAt(LocalDateTime.now());
////
////        Medicine updatedMedicine = medicineRepository.save(medicine);
////        log.warn("Medicine recalled: ID={}, Reason={}", medicineId, reason);
////
////        auditLogUtil.logStatusChange(userId, getUserName(userId), "Medicine", medicineId,
////                medicine.getName(), oldStatus.toString(), "RECALLED");
////
////        return convertToMedicineResponse(updatedMedicine);
////    }
////
////    /**
////     * Mark medicine as damaged
////     */
////    public MedicineResponse markAsDamaged(Long medicineId, Long userId, String reason) {
////        Medicine medicine = medicineRepository.findById(medicineId)
////                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found"));
////
////        MedicineStatus oldStatus = medicine.getStatus();
////        medicine.setStatus(MedicineStatus.DAMAGED);
////        medicine.setUpdatedAt(LocalDateTime.now());
////
////        Medicine updatedMedicine = medicineRepository.save(medicine);
////        log.warn("Medicine marked as damaged: ID={}, Reason={}", medicineId, reason);
////
////        auditLogUtil.logError(userId, "MARK_DAMAGED", "Medicine", reason);
////
////        return convertToMedicineResponse(updatedMedicine);
////    }
////
////    /**
////     * Get medicines statistics
////     */
////    public MedicineStatistics getMedicineStatistics() {
////        long total = getTotalMedicinesCount();
////        long created = getMedicinesCountByStatus(MedicineStatus.CREATED);
////        long inTransit = getMedicinesCountByStatus(MedicineStatus.IN_TRANSIT);
////        long sentToDistributor = getMedicinesCountByStatus(MedicineStatus.SENT_TO_DISTRIBUTOR);
////        long receivedByDistributor = getMedicinesCountByStatus(MedicineStatus.RECEIVED_BY_DISTRIBUTOR);
////        long sentToPharmacy = getMedicinesCountByStatus(MedicineStatus.SENT_TO_PHARMACY);
////        long receivedByPharmacy = getMedicinesCountByStatus(MedicineStatus.RECEIVED_BY_PHARMACY);
////        long sold = getMedicinesCountByStatus(MedicineStatus.SOLD);
////        long damaged = getMedicinesCountByStatus(MedicineStatus.DAMAGED);
////        long recalled = getMedicinesCountByStatus(MedicineStatus.RECALLED);
////
////        return MedicineStatistics.builder()
////                .totalMedicines(total)
////                .createdMedicines(created)
////                .inTransitMedicines(inTransit)
////                .sentToDistributorMedicines(sentToDistributor)
////                .receivedByDistributorMedicines(receivedByDistributor)
////                .sentToPharmacyMedicines(sentToPharmacy)
////                .receivedByPharmacyMedicines(receivedByPharmacy)
////                .soldMedicines(sold)
////                .damagedMedicines(damaged)
////                .recalledMedicines(recalled)
////                .build();
////    }
////
////    /**
////     * Export medicines to list (for downloading/reporting)
////     */
////    public List<MedicineResponse> exportMedicinesByStatus(MedicineStatus status) {
////        log.debug("Exporting medicines with status: {}", status);
////        return medicineRepository.findByStatus(status)
////                .stream()
////                .map(this::convertToMedicineResponse)
////                .collect(Collectors.toList());
////    }
////
////    /**
////     * Export medicines by manufacturer
////     */
////    public List<MedicineResponse> exportMedicinesByManufacturer(Long manufacturerId) {
////        log.debug("Exporting medicines from manufacturer: {}", manufacturerId);
////        return medicineRepository.findByManufacturerId(manufacturerId)
////                .stream()
////                .map(this::convertToMedicineResponse)
////                .collect(Collectors.toList());
////    }
////
////    /**
////     * Generate unique QR code
////     */
////    private String generateUniqueQRCode() {
////        String qrCode;
////        do {
////            qrCode = "QR-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
////        } while (isQrCodeExists(qrCode));
////        return qrCode;
////    }
////
//
//
////    /**
////     * Get user name by ID (helper method)
////     */
////    private String getUserName(Long userId) {
////        return userRepository.findById(userId)
////                .map(User::getName)
////                .orElse("Unknown");
////    }
////}
//
//package com.pharmaTrace.service;
//
//import com.pharmaTrace.dto.request.MedicineRequest;
//import com.pharmaTrace.dto.response.MedicineResponse;
//import com.pharmaTrace.entity.Medicine;
//import com.pharmaTrace.entity.MedicineStatus;
//import com.pharmaTrace.repository.MedicineRepository;
//import com.pharmaTrace.exception.ResourceNotFoundException;
//import com.pharmaTrace.util.AuditLogUtil;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class MedicineService {
//
//    private final MedicineRepository medicineRepository;
//    private final AuditLogUtil auditLogUtil;
//
//    /**
//     * Create new medicine batch
//     */
//    public MedicineResponse createMedicine(MedicineRequest medicineRequest, Long manufacturerId, String manufacturerName) {
//        String uniqueHash = generateUniqueQRCode();
//
//        Medicine medicine = Medicine.builder()
//                .name(medicineRequest.getName())
//                .description(medicineRequest.getDescription())
//                .batchNumber(medicineRequest.getBatchNumber())
//                .manufacturerId(manufacturerId)
//                .manufacturer(manufacturerName)
//                .serialNumber(medicineRequest.getSerialNumber())
//                .expiryDate(medicineRequest.getExpiryDate())
//                .manufacturingDate(medicineRequest.getManufacturingDate())
//                .quantity(medicineRequest.getQuantity())
//                .storageConditions(medicineRequest.getStorageConditions())
//                .qrCode(uniqueHash)
//                .status(MedicineStatus.CREATED)
//                .isVerified(false)
//                .verificationCount(0)
//                .additionalInfo(medicineRequest.getAdditionalInfo())
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .build();
//
//        Medicine savedMedicine = medicineRepository.save(medicine);
//        log.info("Medicine created successfully with ID: {}, QR: {}", savedMedicine.getId(), uniqueHash);
//
//        auditLogUtil.logAudit(manufacturerId, "CREATE_MEDICINE", "Medicine", savedMedicine.getId(),
//                "Medicine batch created: " + savedMedicine.getName(), "SUCCESS");
//
//        return convertToMedicineResponse(savedMedicine);
//    }
//
//    /**
//     * Get medicine by ID
//     */
//    public MedicineResponse getMedicineById(Long medicineId) {
//        Medicine medicine = medicineRepository.findById(medicineId)
//                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with ID: " + medicineId));
//        return convertToMedicineResponse(medicine);
//    }
//
//    /**
//     * Get medicine by QR code
//     */
//    public MedicineResponse getMedicineByQrCode(String qrCode) {
//        Medicine medicine = medicineRepository.findByQrCode(qrCode)
//                .orElseThrow(() -> new ResourceNotFoundException("Invalid QR code: " + qrCode));
//        return convertToMedicineResponse(medicine);
//    }
//
//    /**
//     * Get medicines by manufacturer paginated
//     */
//    public Page<MedicineResponse> getMedicinesByManufacturer(Long manufacturerId, Pageable pageable) {
//        return medicineRepository.findByManufacturerId(manufacturerId, pageable)
//                .map(this::convertToMedicineResponse);
//    }
//
//    /**
//     * Get medicines by status paginated
//     */
//    public Page<MedicineResponse> getMedicinesByStatus(MedicineStatus status, Pageable pageable) {
//        return medicineRepository.findByStatus(status, pageable)
//                .map(this::convertToMedicineResponse);
//    }
//
//    /**
//     * Get all medicines paginated
//     */
//    public Page<MedicineResponse> getAllMedicinesPaginated(Pageable pageable) {
//        return medicineRepository.findAll(pageable)
//                .map(this::convertToMedicineResponse);
//    }
//
//    /**
//     * Search medicines by name
//     */
//    public Page<MedicineResponse> searchMedicines(String name, Pageable pageable) {
//        return medicineRepository.findByNameContainingIgnoreCase(name, pageable)
//                .map(this::convertToMedicineResponse);
//    }
//
//    /**
//     * Update medicine status
//     */
//    public MedicineResponse updateMedicineStatus(Long medicineId, MedicineStatus newStatus, Long userId) {
//        Medicine medicine = medicineRepository.findById(medicineId)
//                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found"));
//
//        MedicineStatus oldStatus = medicine.getStatus();
//        medicine.setStatus(newStatus);
//        medicine.setUpdatedAt(LocalDateTime.now());
//
//        Medicine updatedMedicine = medicineRepository.save(medicine);
//        log.info("Medicine status updated from {} to {} for ID: {}", oldStatus, newStatus, medicineId);
//
//        auditLogUtil.logAudit(userId, "UPDATE_STATUS", "Medicine", medicineId,
//                "Status updated from " + oldStatus + " to " + newStatus, "SUCCESS");
//
//        return convertToMedicineResponse(updatedMedicine);
//    }
//
//    /**
//     * Verify medicine
//     */
//    public MedicineResponse verifyMedicine(Long medicineId, Long pharmacyId) {
//        Medicine medicine = medicineRepository.findById(medicineId)
//                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found"));
//
//        medicine.setIsVerified(true);
//        medicine.setVerificationCount(medicine.getVerificationCount() + 1);
//        medicine.setLastVerifiedAt(LocalDateTime.now());
//        medicine.setUpdatedAt(LocalDateTime.now());
//
//        Medicine updatedMedicine = medicineRepository.save(medicine);
//
//        auditLogUtil.logAudit(pharmacyId, "VERIFY_MEDICINE", "Medicine", medicineId,
//                "Medicine verified by pharmacy", "SUCCESS");
//
//        log.info("Medicine verified: ID={}, VerificationCount={}", medicineId, updatedMedicine.getVerificationCount());
//
//        return convertToMedicineResponse(updatedMedicine);
//    }
//
//    /**
//     * Get all medicines
//     */
//    public Page<MedicineResponse> getAllMedicines(Pageable pageable) {
//        return medicineRepository.findAll(pageable)
//                .map(this::convertToMedicineResponse);
//    }
//
//    /**
//     * Get medicine statistics
//     */
//    public Object getMedicineStatistics() {
//        long total = medicineRepository.count();
//        long created = medicineRepository.countByStatus(MedicineStatus.CREATED);
//        long inTransit = medicineRepository.countByStatus(MedicineStatus.IN_TRANSIT);
//        long sentToDistributor = medicineRepository.countByStatus(MedicineStatus.SENT_TO_DISTRIBUTOR);
//        long receivedByDistributor = medicineRepository.countByStatus(MedicineStatus.RECEIVED_BY_DISTRIBUTOR);
//        long sentToPharmacy = medicineRepository.countByStatus(MedicineStatus.SENT_TO_PHARMACY);
//        long receivedByPharmacy = medicineRepository.countByStatus(MedicineStatus.RECEIVED_BY_PHARMACY);
//        long sold = medicineRepository.countByStatus(MedicineStatus.SOLD);
//        long damaged = medicineRepository.countByStatus(MedicineStatus.DAMAGED);
//        long recalled = medicineRepository.countByStatus(MedicineStatus.RECALLED);
//
//        return new Object() {
//            public final long totalMedicines = total;
//            public final long createdMedicines = created;
//            public final long inTransitMedicines = inTransit;
//            public final long sentToDistributorMedicines = sentToDistributor;
//            public final long receivedByDistributorMedicines = receivedByDistributor;
//            public final long sentToPharmacyMedicines = sentToPharmacy;
//            public final long receivedByPharmacyMedicines = receivedByPharmacy;
//            public final long soldMedicines = sold;
//            public final long damagedMedicines = damaged;
//            public final long recalledMedicines = recalled;
//        };
//    }
//
//    /**
//     * Convert Medicine entity to MedicineResponse DTO
//     */
//    private MedicineResponse convertToMedicineResponse(Medicine medicine) {
//        return MedicineResponse.builder()
//                .id(medicine.getId())
//                .name(medicine.getName())
//                .description(medicine.getDescription())
//                .batchNumber(medicine.getBatchNumber())
//                .manufacturerId(medicine.getManufacturerId())
//                .manufacturer(medicine.getManufacturer())
//                .serialNumber(medicine.getSerialNumber())
//                .expiryDate(medicine.getExpiryDate())
//                .manufacturingDate(medicine.getManufacturingDate())
//                .quantity(medicine.getQuantity())
//                .storageConditions(medicine.getStorageConditions())
//                .status(medicine.getStatus())
//                .qrCode(medicine.getQrCode())
//                .isVerified(medicine.getIsVerified())
//                .verificationCount(medicine.getVerificationCount())
//                .lastVerifiedAt(medicine.getLastVerifiedAt())
//                .createdAt(medicine.getCreatedAt())
//                .updatedAt(medicine.getUpdatedAt())
//                .additionalInfo(medicine.getAdditionalInfo())
//                .build();
//    }
//
//    /**
//     * Generate unique QR code
//     */
//    private String generateUniqueQRCode() {
//        String qrCode;
//        do {
//            qrCode = "QR-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
//        } while (medicineRepository.findByQrCode(qrCode).isPresent());
//        return qrCode;
//    }
//}


package com.pharmaTrace.service;

import com.pharmaTrace.dto.request.MedicineRequest;
import com.pharmaTrace.dto.response.MedicineResponse;
import com.pharmaTrace.entity.Medicine;
import com.pharmaTrace.entity.MedicineStatus;
import com.pharmaTrace.exception.ResourceNotFoundException;
import com.pharmaTrace.repository.MedicineRepository;
import com.pharmaTrace.util.AuditLogUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MedicineService {

    private final MedicineRepository medicineRepository;
    private final AuditLogUtil auditLogUtil;

    /**
     * Create new medicine batch
     */
    public MedicineResponse createMedicine(MedicineRequest medicineRequest,
                                           Long manufacturerId,
                                           String manufacturerName) {

        String qrCode = generateUniqueQRCode();

        Medicine medicine = Medicine.builder()
                .name(medicineRequest.getName())
                .description(medicineRequest.getDescription())
                .batchNumber(medicineRequest.getBatchNumber())
                .manufacturerId(manufacturerId)
                .manufacturer(manufacturerName)
                .serialNumber(medicineRequest.getSerialNumber())
                .expiryDate(medicineRequest.getExpiryDate())
                .manufacturingDate(medicineRequest.getManufacturingDate())
                .quantity(medicineRequest.getQuantity())
                .storageConditions(medicineRequest.getStorageConditions())
                .additionalInfo(medicineRequest.getAdditionalInfo())
                .qrCode(qrCode)
                .status(MedicineStatus.CREATED)
                .isVerified(false)
                .verificationCount(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Medicine savedMedicine = medicineRepository.save(medicine);

        auditLogUtil.logAudit(
                manufacturerId,
                "CREATE_MEDICINE",
                "Medicine",
                savedMedicine.getId(),
                "Medicine created with batch number: " + savedMedicine.getBatchNumber(),
                "SUCCESS"
        );

        log.info("Medicine created successfully with ID: {}", savedMedicine.getId());

        return convertToResponse(savedMedicine);
    }

    /**
     * Get medicine by ID
     */
    public MedicineResponse getMedicineById(Long medicineId) {
        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with ID: " + medicineId));

        return convertToResponse(medicine);
    }

    /**
     * Get medicine entity by ID
     */
    public Medicine getMedicineEntityById(Long medicineId) {
        return medicineRepository.findById(medicineId)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with ID: " + medicineId));
    }

    /**
     * Get medicine by QR code
     */
    public MedicineResponse getMedicineByQrCode(String qrCode) {
        Medicine medicine = medicineRepository.findByQrCode(qrCode)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with QR code: " + qrCode));

        return convertToResponse(medicine);
    }

    /**
     * Get medicines by manufacturer
     */
    public Page<MedicineResponse> getMedicinesByManufacturer(Long manufacturerId, Pageable pageable) {
        return medicineRepository.findByManufacturerId(manufacturerId, pageable)
                .map(this::convertToResponse);
    }

    /**
     * Get medicines by status
     */
    public Page<MedicineResponse> getMedicinesByStatus(MedicineStatus status, Pageable pageable) {
        return medicineRepository.findByStatus(status, pageable)
                .map(this::convertToResponse);
    }

    /**
     * Get all medicines
     */
    public Page<MedicineResponse> getAllMedicines(Pageable pageable) {
        return medicineRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    /**
     * Search medicines by name
     */
    public Page<MedicineResponse> searchMedicines(String keyword, Pageable pageable) {
        return medicineRepository.findByNameContainingIgnoreCase(keyword, pageable)
                .map(this::convertToResponse);
    }

    /**
     * Update medicine details
     */
    public MedicineResponse updateMedicine(Long medicineId,
                                           MedicineRequest medicineRequest,
                                           Long userId) {

        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found"));

        medicine.setName(medicineRequest.getName());
        medicine.setDescription(medicineRequest.getDescription());
        medicine.setBatchNumber(medicineRequest.getBatchNumber());
        medicine.setSerialNumber(medicineRequest.getSerialNumber());
        medicine.setManufacturingDate(medicineRequest.getManufacturingDate());
        medicine.setExpiryDate(medicineRequest.getExpiryDate());
        medicine.setQuantity(medicineRequest.getQuantity());
        medicine.setStorageConditions(medicineRequest.getStorageConditions());
        medicine.setAdditionalInfo(medicineRequest.getAdditionalInfo());
        medicine.setUpdatedAt(LocalDateTime.now());

        Medicine updatedMedicine = medicineRepository.save(medicine);

        auditLogUtil.logAudit(
                userId,
                "UPDATE_MEDICINE",
                "Medicine",
                medicineId,
                "Medicine updated successfully",
                "SUCCESS"
        );

        return convertToResponse(updatedMedicine);
    }

    /**
     * Update medicine status
     */
    public MedicineResponse updateMedicineStatus(Long medicineId,
                                                 MedicineStatus newStatus,
                                                 Long userId) {

        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found"));

        MedicineStatus oldStatus = medicine.getStatus();

        medicine.setStatus(newStatus);
        medicine.setUpdatedAt(LocalDateTime.now());

        Medicine updatedMedicine = medicineRepository.save(medicine);

        auditLogUtil.logAudit(
                userId,
                "UPDATE_MEDICINE_STATUS",
                "Medicine",
                medicineId,
                "Status changed from " + oldStatus + " to " + newStatus,
                "SUCCESS"
        );

        return convertToResponse(updatedMedicine);
    }

    /**
     * Verify medicine
     */
    public MedicineResponse verifyMedicine(Long medicineId, Long pharmacyId) {
        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found"));

        medicine.setIsVerified(true);
        medicine.setVerificationCount(medicine.getVerificationCount() + 1);
        medicine.setLastVerifiedAt(LocalDateTime.now());
        medicine.setUpdatedAt(LocalDateTime.now());

        Medicine updatedMedicine = medicineRepository.save(medicine);

        auditLogUtil.logAudit(
                pharmacyId,
                "VERIFY_MEDICINE",
                "Medicine",
                medicineId,
                "Medicine verified successfully",
                "SUCCESS"
        );

        return convertToResponse(updatedMedicine);
    }

    /**
     * Recall medicine
     */
    public MedicineResponse recallMedicine(Long medicineId,
                                           String reason,
                                           Long userId) {

        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found"));

        medicine.setStatus(MedicineStatus.RECALLED);
        medicine.setUpdatedAt(LocalDateTime.now());

        Medicine updatedMedicine = medicineRepository.save(medicine);

        auditLogUtil.logAudit(
                userId,
                "RECALL_MEDICINE",
                "Medicine",
                medicineId,
                "Medicine recalled. Reason: " + reason,
                "SUCCESS"
        );

        return convertToResponse(updatedMedicine);
    }

    /**
     * Mark medicine as damaged
     */
    public MedicineResponse markMedicineAsDamaged(Long medicineId,
                                                  String reason,
                                                  Long userId) {

        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found"));

        medicine.setStatus(MedicineStatus.DAMAGED);
        medicine.setUpdatedAt(LocalDateTime.now());

        Medicine updatedMedicine = medicineRepository.save(medicine);

        auditLogUtil.logAudit(
                userId,
                "MARK_DAMAGED_MEDICINE",
                "Medicine",
                medicineId,
                "Medicine marked as damaged. Reason: " + reason,
                "SUCCESS"
        );

        return convertToResponse(updatedMedicine);
    }

    /**
     * Delete medicine
     */
    public void deleteMedicine(Long medicineId, Long userId) {
        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found"));

        medicineRepository.delete(medicine);

        auditLogUtil.logAudit(
                userId,
                "DELETE_MEDICINE",
                "Medicine",
                medicineId,
                "Medicine deleted successfully",
                "SUCCESS"
        );
    }

    /**
     * Get recent medicines
     */
    public List<MedicineResponse> getRecentMedicines() {
        return medicineRepository.findTop10ByOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get medicine statistics
     */
    public Object getMedicineStatistics() {
        return new Object() {
            public final long totalMedicines = medicineRepository.count();
            public final long createdMedicines = medicineRepository.countByStatus(MedicineStatus.CREATED);
            public final long inTransitMedicines = medicineRepository.countByStatus(MedicineStatus.IN_TRANSIT);
            public final long soldMedicines = medicineRepository.countByStatus(MedicineStatus.SOLD);
            public final long recalledMedicines = medicineRepository.countByStatus(MedicineStatus.RECALLED);
            public final long damagedMedicines = medicineRepository.countByStatus(MedicineStatus.DAMAGED);
            public final long verifiedMedicines = medicineRepository.countByIsVerifiedTrue();
        };
    }

    /**
     * Get dashboard summary
     */
    public Object getDashboard(Long manufacturerId) {
        return new Object() {
            public final long totalMedicines = medicineRepository.countByManufacturerId(manufacturerId);
            public final List<MedicineResponse> recentMedicines = getRecentMedicines();
        };
    }

    private MedicineResponse convertToResponse(Medicine medicine) {
        return MedicineResponse.builder()
                .id(medicine.getId())
                .name(medicine.getName())
                .description(medicine.getDescription())
                .batchNumber(medicine.getBatchNumber())
                .manufacturerId(medicine.getManufacturerId())
                .manufacturer(medicine.getManufacturer())
                .serialNumber(medicine.getSerialNumber())
                .manufacturingDate(medicine.getManufacturingDate())
                .expiryDate(medicine.getExpiryDate())
                .quantity(medicine.getQuantity())
                .storageConditions(medicine.getStorageConditions())
                .additionalInfo(medicine.getAdditionalInfo())
                .qrCode(medicine.getQrCode())
                .status(medicine.getStatus())
                .isVerified(medicine.getIsVerified())
                .verificationCount(medicine.getVerificationCount())
                .lastVerifiedAt(medicine.getLastVerifiedAt())
                .createdAt(medicine.getCreatedAt())
                .updatedAt(medicine.getUpdatedAt())
                .build();
    }

    private String generateUniqueQRCode() {
        String qrCode;

        do {
            qrCode = "QR-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
        } while (medicineRepository.findByQrCode(qrCode).isPresent());

        return qrCode;
    }
}
