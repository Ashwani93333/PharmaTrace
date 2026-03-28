//package com.pharmaTrace.service;
//
//import com.pharmaTrace.dto.request.ShipmentRequest;
//import com.pharmaTrace.dto.response.ShipmentResponse;
//import com.pharmaTrace.entity.Shipment;
//import com.pharmaTrace.entity.Medicine;
//import com.pharmaTrace.entity.MedicineStatus;
//import com.pharmaTrace.entity.ShipmentHistory;
//import com.pharmaTrace.exception.ResourceNotFoundException;
//import com.pharmaTrace.repository.ShipmentRepository;
//import com.pharmaTrace.repository.MedicineRepository;
//import com.pharmaTrace.repository.ShipmentHistoryRepository;
//import com.pharmaTrace.util.AuditLogUtil;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class DistributorService {
//
//    private final ShipmentRepository shipmentRepository;
//    private final MedicineRepository medicineRepository;
//    private final ShipmentHistoryRepository shipmentHistoryRepository;
//    private final AuditLogUtil auditLogUtil;
//
//    /**
//     * CREATE NEW SHIPMENT
//     * Distributor accepts medicine from manufacturer and creates shipment
//     * Automatically assigns shipment number and sets initial status as PENDING
//     */
//    public ShipmentResponse createShipment(ShipmentRequest shipmentRequest, Long distributorId, String distributorName) {
//        log.info("Creating new shipment for medicine ID: {}, Distributor: {}",
//                shipmentRequest.getMedicineId(), distributorName);
//
//        // Validate medicine exists
//        Medicine medicine = medicineRepository.findById(shipmentRequest.getMedicineId())
//                .orElseThrow(() -> {
//                    log.error("Medicine not found with ID: {}", shipmentRequest.getMedicineId());
//                    return new ResourceNotFoundException("Medicine not found with ID: " + shipmentRequest.getMedicineId());
//                });
//
//        log.debug("Medicine found: {}, Batch: {}", medicine.getName(), medicine.getBatchNumber());
//
//        // Create shipment entity
//        Shipment shipment = Shipment.builder()
//                .medicineId(medicine.getId())
//                .medicineName(medicine.getName())
//                .batchNumber(medicine.getBatchNumber())
//                .fromUserId(medicine.getManufacturerId())
//                .fromUserName(medicine.getManufacturer())
//                .toUserId(shipmentRequest.getToUserId())
//                .toUserName("Pharmacy")
//                .distributorId(distributorId)
//                .distributorName(distributorName)
//                .status("PENDING")
//                .pickupLocation(shipmentRequest.getPickupLocation())
//                .deliveryLocation(shipmentRequest.getDeliveryLocation())
//                .currentLocation(shipmentRequest.getPickupLocation())
//                .transportMode(shipmentRequest.getTransportMode())
//                .vehicleNumber(shipmentRequest.getVehicleNumber())
//                .driverInfo(shipmentRequest.getDriverInfo())
//                .temperatureRange(shipmentRequest.getTemperatureRange())
//                .specialInstructions(shipmentRequest.getSpecialInstructions())
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .build();
//
//        // Save shipment
//        Shipment savedShipment = shipmentRepository.save(shipment);
//        log.info("Shipment saved successfully with ID: {}, Shipment Number: {}",
//                savedShipment.getId(), savedShipment.getShipmentNumber());
//
//        // Update medicine status to SENT_TO_DISTRIBUTOR
//        medicine.setStatus(MedicineStatus.SENT_TO_DISTRIBUTOR);
//        medicineRepository.save(medicine);
//        log.debug("Medicine status updated to SENT_TO_DISTRIBUTOR");
//
//        // Create shipment history entry
//        createShipmentHistory(savedShipment.getId(), "PENDING",
//                shipmentRequest.getPickupLocation(), null,
//                "Shipment created - pending pickup", distributorId);
//
//        // Audit log
//        auditLogUtil.logAudit(distributorId, "CREATE_SHIPMENT", "Shipment", savedShipment.getId(),
//                "Shipment created for medicine: " + medicine.getName() + ", Shipment#: " + savedShipment.getShipmentNumber(),
//                "SUCCESS");
//
//        log.info("Shipment creation completed. Shipment#: {}", savedShipment.getShipmentNumber());
//
//        return convertToShipmentResponse(savedShipment);
//    }
//
//    /**
//     * UPDATE SHIPMENT STATUS
//     * Track medicine movement with real-time updates
//     * Records location, GPS coordinates, and status changes
//     */
//    public ShipmentResponse updateShipmentStatus(Long shipmentId, String status, String location,
//                                                 String gpsCoordinates, String notes, Long distributorId) {
//        log.info("Updating shipment ID: {} to status: {}", shipmentId, status);
//
//        // Find shipment
//        Shipment shipment = shipmentRepository.findById(shipmentId)
//                .orElseThrow(() -> {
//                    log.error("Shipment not found with ID: {}", shipmentId);
//                    return new ResourceNotFoundException("Shipment not found with ID: " + shipmentId);
//                });
//
//        String previousStatus = shipment.getStatus();
//        log.debug("Previous status: {}, New status: {}", previousStatus, status);
//
//        // Update shipment
//        shipment.setStatus(status);
//        if (location != null && !location.isEmpty()) {
//            shipment.setCurrentLocation(location);
//            log.debug("Location updated: {}", location);
//        }
//        if (gpsCoordinates != null && !gpsCoordinates.isEmpty()) {
//            shipment.setGpsCoordinates(gpsCoordinates);
//            log.debug("GPS updated: {}", gpsCoordinates);
//        }
//        if (notes != null && !notes.isEmpty()) {
//            shipment.setNotes(notes);
//            log.debug("Notes updated: {}", notes);
//        }
//        shipment.setUpdatedAt(LocalDateTime.now());
//
//        // Save updated shipment
//        Shipment updatedShipment = shipmentRepository.save(shipment);
//        log.info("Shipment updated successfully");
//
//        // Create history entry
//        createShipmentHistory(shipmentId, status, location, gpsCoordinates, notes, distributorId);
//
//        // Update medicine status based on shipment status
//        if ("DELIVERED".equals(status)) {
//            Medicine medicine = medicineRepository.findById(shipment.getMedicineId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Medicine not found"));
//            medicine.setStatus(MedicineStatus.RECEIVED_BY_PHARMACY);
//            medicineRepository.save(medicine);
//            log.debug("Medicine status updated to RECEIVED_BY_PHARMACY");
//        }
//
//        // Audit log
//        auditLogUtil.logAudit(distributorId, "UPDATE_SHIPMENT_STATUS", "Shipment", shipmentId,
//                "Status: " + previousStatus + " → " + status + ", Location: " + location, "SUCCESS");
//
//        log.info("Shipment status update completed");
//
//        return convertToShipmentResponse(updatedShipment);
//    }
//
//    /**
//     * GET ALL SHIPMENTS BY DISTRIBUTOR
//     * Retrieve paginated list of all shipments for this distributor
//     */
//    public Page<ShipmentResponse> getShipmentsByDistributor(Long distributorId, Pageable pageable) {
//        log.debug("Fetching shipments for distributor ID: {}, Page: {}, Size: {}",
//                distributorId, pageable.getPageNumber(), pageable.getPageSize());
//
//        Page<Shipment> shipments = shipmentRepository.findByDistributorId(distributorId, pageable);
//        log.info("Retrieved {} shipments for distributor", shipments.getTotalElements());
//
//        return shipments.map(this::convertToShipmentResponse);
//    }
//
//    /**
//     * GET PENDING SHIPMENTS
//     * Shipments awaiting pickup (Status: PENDING)
//     */
//    public Page<ShipmentResponse> getPendingShipments(Long distributorId, Pageable pageable) {
//        log.debug("Fetching pending shipments for distributor ID: {}", distributorId);
//
//        Page<Shipment> shipments = shipmentRepository.findByDistributorIdAndStatus(distributorId, "PENDING", pageable);
//        log.info("Retrieved {} pending shipments", shipments.getTotalElements());
//
//        return shipments.map(this::convertToShipmentResponse);
//    }
//
//    /**
//     * GET IN-TRANSIT SHIPMENTS
//     * Shipments currently on the road (Status: IN_TRANSIT)
//     */
//    public Page<ShipmentResponse> getInTransitShipments(Long distributorId, Pageable pageable) {
//        log.debug("Fetching in-transit shipments for distributor ID: {}", distributorId);
//
//        Page<Shipment> shipments = shipmentRepository.findByDistributorIdAndStatus(distributorId, "IN_TRANSIT", pageable);
//        log.info("Retrieved {} in-transit shipments", shipments.getTotalElements());
//
//        return shipments.map(this::convertToShipmentResponse);
//    }
//
//    /**
//     * GET DELIVERED SHIPMENTS
//     * Completed deliveries (Status: DELIVERED)
//     */
//    public Page<ShipmentResponse> getDeliveredShipments(Long distributorId, Pageable pageable) {
//        log.debug("Fetching delivered shipments for distributor ID: {}", distributorId);
//
//        Page<Shipment> shipments = shipmentRepository.findByDistributorIdAndStatus(distributorId, "DELIVERED", pageable);
//        log.info("Retrieved {} delivered shipments", shipments.getTotalElements());
//
//        return shipments.map(this::convertToShipmentResponse);
//    }
//
//    /**
//     * GET SHIPMENT BY ID
//     * Retrieve complete details of specific shipment
//     */
//    public ShipmentResponse getShipmentById(Long shipmentId) {
//        log.debug("Fetching shipment with ID: {}", shipmentId);
//
//        Shipment shipment = shipmentRepository.findById(shipmentId)
//                .orElseThrow(() -> {
//                    log.error("Shipment not found with ID: {}", shipmentId);
//                    return new ResourceNotFoundException("Shipment not found with ID: " + shipmentId);
//                });
//
//        log.info("Shipment retrieved successfully: {}", shipmentId);
//
//        return convertToShipmentResponse(shipment);
//    }
//
//    /**
//     * CONFIRM DELIVERY
//     * Mark shipment as delivered to pharmacy
//     * Updates timestamp and delivery notes
//     */
//    public ShipmentResponse confirmDelivery(Long shipmentId, String deliveryNotes, Long distributorId) {
//        log.info("Confirming delivery for shipment ID: {}", shipmentId);
//
//        // Find shipment
//        Shipment shipment = shipmentRepository.findById(shipmentId)
//                .orElseThrow(() -> {
//                    log.error("Shipment not found with ID: {}", shipmentId);
//                    return new ResourceNotFoundException("Shipment not found with ID: " + shipmentId);
//                });
//
//        // Update shipment status
//        shipment.setStatus("DELIVERED");
//        shipment.setDeliveredAt(LocalDateTime.now());
//        if (deliveryNotes != null && !deliveryNotes.isEmpty()) {
//            shipment.setDeliveryNotes(deliveryNotes);
//            log.debug("Delivery notes recorded: {}", deliveryNotes);
//        }
//        shipment.setUpdatedAt(LocalDateTime.now());
//
//        // Save
//        Shipment updatedShipment = shipmentRepository.save(shipment);
//        log.info("Delivery confirmed at: {}", updatedShipment.getDeliveredAt());
//
//        // Create history
//        createShipmentHistory(shipmentId, "DELIVERED",
//                shipment.getDeliveryLocation(), null,
//                "Delivery confirmed - " + deliveryNotes, distributorId);
//
//        // Update medicine status
//        Medicine medicine = medicineRepository.findById(shipment.getMedicineId())
//                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found"));
//        medicine.setStatus(MedicineStatus.RECEIVED_BY_PHARMACY);
//        medicineRepository.save(medicine);
//
//        // Audit log
//        auditLogUtil.logAudit(distributorId, "CONFIRM_DELIVERY", "Shipment", shipmentId,
//                "Delivery confirmed. Notes: " + deliveryNotes, "SUCCESS");
//
//        log.info("Delivery confirmation completed");
//
//        return convertToShipmentResponse(updatedShipment);
//    }
//
//    /**
//     * GET SHIPMENT LOCATION
//     * Real-time GPS location and current status
//     */
//    public Object getShipmentLocation(Long shipmentId) {
//        log.debug("Fetching location for shipment ID: {}", shipmentId);
//
//        Shipment shipment = shipmentRepository.findById(shipmentId)
//                .orElseThrow(() -> {
//                    log.error("Shipment not found with ID: {}", shipmentId);
//                    return new ResourceNotFoundException("Shipment not found with ID: " + shipmentId);
//                });
//
//        log.info("Location retrieved for shipment: {}", shipmentId);
//
//        return new Object() {
//            public final Long shipmentId = shipment.getId();
//            public final String shipmentNumber = shipment.getShipmentNumber();
//            public final String currentLocation = shipment.getCurrentLocation();
//            public final String gpsCoordinates = shipment.getGpsCoordinates();
//            public final String status = shipment.getStatus();
//            public final String medicineName = shipment.getMedicineName();
//            public final String vehicleNumber = shipment.getVehicleNumber();
//            public final String driverInfo = shipment.getDriverInfo();
//            public final LocalDateTime updatedAt = shipment.getUpdatedAt();
//        };
//    }
//
//    /**
//     * SEARCH SHIPMENTS
//     * Search by shipment number or medicine name
//     */
//    public Page<ShipmentResponse> searchShipments(String query, Long distributorId, Pageable pageable) {
//        log.debug("Searching shipments with query: '{}' for distributor ID: {}", query, distributorId);
//
//        Page<Shipment> shipments = shipmentRepository.searchShipments(distributorId, query, pageable);
//        log.info("Search found {} results", shipments.getTotalElements());
//
//        return shipments.map(this::convertToShipmentResponse);
//    }
//
//    /**
//     * GET SHIPMENT STATISTICS
//     * Dashboard metrics: total, pending, in-transit, delivered
//     */
//    public Object getShipmentStatistics(Long distributorId) {
//        log.debug("Calculating statistics for distributor ID: {}", distributorId);
//
//        // Get total shipments
//        Page<Shipment> allShipments = shipmentRepository.findByDistributorId(distributorId, Pageable.unpaged());
//        long total = allShipments.getTotalElements();
//
//        // Count by status
//        long pending = shipmentRepository.countByDistributorIdAndStatus(distributorId, "PENDING");
//        long inTransit = shipmentRepository.countByDistributorIdAndStatus(distributorId, "IN_TRANSIT");
//        long delivered = shipmentRepository.countByDistributorIdAndStatus(distributorId, "DELIVERED");
//
//        log.info("Statistics calculated - Total: {}, Pending: {}, In-Transit: {}, Delivered: {}",
//                total, pending, inTransit, delivered);
//
//        return new Object() {
//            public final long totalShipments = total;
//            public final long pendingShipments = pending;
//            public final long inTransitShipments = inTransit;
//            public final long deliveredShipments = delivered;
//            public final double deliveryRate = total > 0 ? (delivered * 100.0 / total) : 0;
//        };
//    }
//
//    /**
//     * GET SHIPMENT HISTORY
//     * Complete audit trail of shipment status changes
//     */
//    public Object getShipmentHistory(Long shipmentId) {
//        log.debug("Fetching history for shipment ID: {}", shipmentId);
//
//        // Verify shipment exists
//        Shipment shipment = shipmentRepository.findById(shipmentId)
//                .orElseThrow(() -> {
//                    log.error("Shipment not found with ID: {}", shipmentId);
//                    return new ResourceNotFoundException("Shipment not found");
//                });
//
//        // Get history
//        List<ShipmentHistory> histories = shipmentHistoryRepository.findByShipmentIdOrderByCreatedAtAsc(shipmentId);
//        log.info("Retrieved {} history records for shipment", histories.size());
//
//        return new Object() {
//            public final String shipmentNumber = shipment.getShipmentNumber();
//            public final String medicineName = shipment.getMedicineName();
//            public final String currentStatus = shipment.getStatus();
//            public final List<Object> history = histories.stream().map(h -> new Object() {
//                public final String status = h.getStatus();
//                public final String location = h.getLocation();
//                public final String gpsCoordinates = h.getGpsCoordinates();
//                public final String notes = h.getNotes();
//                public final LocalDateTime createdAt = h.getCreatedAt();
//            }).collect(Collectors.toList());
//        };
//    }
//
//    /**
//     * GET SHIPMENTS BY PHARMACY
//     * All shipments destined for specific pharmacy
//     */
//    public Page<ShipmentResponse> getShipmentsByPharmacy(Long pharmacyId, Long distributorId, Pageable pageable) {
//        log.debug("Fetching shipments for pharmacy ID: {}", pharmacyId);
//
//        Page<Shipment> shipments = shipmentRepository.findByDistributorIdAndToUserId(distributorId, pharmacyId, pageable);
//        log.info("Retrieved {} shipments for pharmacy", shipments.getTotalElements());
//
//        return shipments.map(this::convertToShipmentResponse);
//    }
//
//    /**
//     * GET VEHICLES
//     * List of available vehicles for distributor
//     */
//    public Object getVehicles(Long distributorId) {
//        log.debug("Fetching vehicles for distributor ID: {}", distributorId);
//
//        return new Object() {
//            public final String message = "Vehicles list";
//            public final Object data = new Object() {
//                public final String[] vehicleTypes = {"Refrigerated Van", "Cold Storage Truck", "Regular Van", "Motorcycle"};
//                public final Integer totalVehicles = 5;
//            };
//        };
//    }
//
//    /**
//     * GET DRIVERS
//     * List of available drivers for distributor
//     */
//    public Object getDrivers(Long distributorId) {
//        log.debug("Fetching drivers for distributor ID: {}", distributorId);
//
//        return new Object() {
//            public final String message = "Drivers list";
//            public final Object data = new Object() {
//                public final String[] driverNames = {"Rajesh Kumar", "Amit Patel", "Suresh Singh", "Vikram Sharma"};
//                public final Integer totalDrivers = 4;
//                public final Integer availableDrivers = 3;
//            };
//        };
//    }
//
//    /**
//     * CREATE SHIPMENT HISTORY ENTRY
//     * Internal method to record status changes for audit trail
//     */
//    private void createShipmentHistory(Long shipmentId, String status, String location,
//                                       String gpsCoordinates, String notes, Long distributorId) {
//        try {
//            ShipmentHistory history = ShipmentHistory.builder()
//                    .shipmentId(shipmentId)
//                    .status(status)
//                    .location(location)
//                    .gpsCoordinates(gpsCoordinates)
//                    .notes(notes)
//                    .updatedBy(distributorId)
//                    .createdAt(LocalDateTime.now())
//                    .build();
//
//            shipmentHistoryRepository.save(history);
//            log.debug("Shipment history created for shipment ID: {}, Status: {}", shipmentId, status);
//        } catch (Exception e) {
//            log.error("Error creating shipment history: {}", e.getMessage());
//        }
//    }
//
//    /**
//     * CONVERT SHIPMENT ENTITY TO RESPONSE DTO
//     * Maps Shipment entity to ShipmentResponse for API response
//     */
//    private ShipmentResponse convertToShipmentResponse(Shipment shipment) {
//        return ShipmentResponse.builder()
//                .id(shipment.getId())
//                .shipmentNumber(shipment.getShipmentNumber())
//                .medicineId(shipment.getMedicineId())
//                .medicineName(shipment.getMedicineName())
//                .batchNumber(shipment.getBatchNumber())
//                .fromUserId(shipment.getFromUserId())
//                .fromUserName(shipment.getFromUserName())
//                .toUserId(shipment.getToUserId())
//                .toUserName(shipment.getToUserName())
//                .distributorId(shipment.getDistributorId())
//                .distributorName(shipment.getDistributorName())
//                .status(shipment.getStatus())
//                .pickupLocation(shipment.getPickupLocation())
//                .deliveryLocation(shipment.getDeliveryLocation())
//                .currentLocation(shipment.getCurrentLocation())
//                .gpsCoordinates(shipment.getGpsCoordinates())
//                .transportMode(shipment.getTransportMode())
//                .vehicleNumber(shipment.getVehicleNumber())
//                .driverInfo(shipment.getDriverInfo())
//                .temperatureRange(shipment.getTemperatureRange())
//                .specialInstructions(shipment.getSpecialInstructions())
//                .notes(shipment.getNotes())
//                .deliveryNotes(shipment.getDeliveryNotes())
//                .createdAt(shipment.getCreatedAt())
//                .updatedAt(shipment.getUpdatedAt())
//                .deliveredAt(shipment.getDeliveredAt())
//                .build();
//    }
//}