//package com.pharmaTrace.entity;
//
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.apache.catalina.User;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "tracking_logs", indexes = {
//        @Index(name = "idx_medicine_id", columnList = "medicine_id"),
//        @Index(name = "idx_status", columnList = "status"),
//        @Index(name = "idx_timestamp", columnList = "timestamp")
//})
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class TrackingLog {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "medicine_id", nullable = false)
//    private Medicine medicine;
//
////    @ManyToOne(fetch = FetchType.LAZY)
////    private Long medicineId;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private MedicineStatus status;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "updated_by", nullable = false)
//    private Long updatedBy;
//
//    @Column(name = "timestamp", nullable = false, updatable = false)
//    private LocalDateTime timestamp;
//
//    @Column(length = 500)
//    private String remarks;
//
//    @Column(name = "location")
//    private String location;
//
//    @PrePersist
//    protected void onCreate() {
//        timestamp = LocalDateTime.now();
//    }
//
//}


package com.pharmaTrace.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tracking_logs", indexes = {
        @Index(name = "idx_medicine_id", columnList = "medicine_id"),
        @Index(name = "idx_updated_by", columnList = "updated_by"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_timestamp", columnList = "timestamp"),
        @Index(name = "idx_created_at", columnList = "created_at"),
        @Index(name = "idx_qr_code_hash", columnList = "qr_code_hash")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackingLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "medicine_id", nullable = false)
    private Long medicineId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id", nullable = false, insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_tracking_medicine"))
    private Medicine medicine;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private MedicineStatus status;

    @Column(name = "updated_by", nullable = false)
    private Long updatedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", nullable = false, insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_tracking_user"))
    private User updatedByUser;

    @Column(name = "updated_by_name", length = 100, nullable = false)
    private String updatedByName;

    @Column(name = "updated_by_role", length = 50)
    private String updatedByRole;

    @Column(name = "timestamp", nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "location", length = 100)
    private String location;

    @Column(name = "gps_coordinates", length = 100)
    private String gpsCoordinates;

    @Column(name = "qr_code_hash", length = 255)
    private String qrCodeHash;

    @Column(length = 500)
    private String remarks;

    @Column(columnDefinition = "TEXT")
    private String metadata;

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

}