package com.pharmaTrace.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "shipments", indexes = {
        @Index(name = "idx_from_role", columnList = "from_role"),
        @Index(name = "idx_to_role", columnList = "to_role"),
        @Index(name = "idx_medicine_id_ship", columnList = "medicine_id"),
        @Index(name = "idx_status_ship", columnList = "status"),
        @Index(name = "idx_shipment_id", columnList = "shipment_number", unique = true)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String shipmentNumber;

    @NotNull(message = "From Role is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role fromRole;

    @NotNull(message = "To Role is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role toRole;

    @NotNull(message = "Medicine ID is required")
    @Column(nullable = false)
    private Long medicineId;

    @NotNull(message = "From User ID is required")
    @Column(nullable = false)
    private Long fromUserId;


    private Long distributorId;

    private LocalDate shipmentDate;

    @Column(length = 100)
    private String fromUserName;

    @Column(length = 100)
    private Long toUserId;

    @Column(length = 100)
    private String toUserName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ShipmentStatus status = ShipmentStatus.PENDING;

    @Column(length = 500)
    private String pickupLocation;

    @Column(length = 500)
    private String deliveryLocation;

    @Column(columnDefinition = "TEXT")
    private String trackingDetails;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column
    private LocalDateTime dispatchedAt;

    @Column
    private LocalDateTime deliveredAt;

    @Column(columnDefinition = "TEXT")
    private String comments;

    @Column(length = 50)
    private String transportMode;

    private Long manufacturerId;



    @Column(length = 100)
    private String vehicleNumber;
//    @ManyToOne
//    @JoinColumn(name = "shipment_id")
//    private Shipment shipment;
}
