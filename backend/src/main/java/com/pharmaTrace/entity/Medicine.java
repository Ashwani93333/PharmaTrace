//////package com.pharmaTrace.entity;
//////
//////import jakarta.persistence.*;
//////import lombok.AllArgsConstructor;
//////import lombok.Builder;
//////import lombok.Data;
//////import lombok.NoArgsConstructor;
//////import org.apache.catalina.User;
//////
//////import java.time.LocalDateTime;
//////
//////@Entity
//////@Table(name = "medicines", indexes = {
//////        @Index(name = "idx_batch_number", columnList = "batch_number"),
//////        @Index(name = "idx_manufacturer_id", columnList = "manufacturer_id"),
//////        @Index(name = "idx_qr_code", columnList = "qr_code")
//////})
//////@Data
//////@NoArgsConstructor
//////@AllArgsConstructor
//////@Builder
//////public class Medicine {
//////
//////    @Id
//////    @GeneratedValue(strategy = GenerationType.IDENTITY)
//////    private Long id;
//////
//////    @Column(nullable = false)
//////    private String name;
//////
//////    @Column(nullable = false)
//////    private String batchNumber;
//////
//////    @ManyToOne(fetch = FetchType.LAZY)
//////    @JoinColumn(name = "manufacturer_id", nullable = false)
//////    private User manufacturer;
//////
//////    @Column(nullable = false, unique = true)
//////    private String qrCode;
//////
//////    @Column(nullable = false)
//////    private Integer quantity;
//////
//////    @Column(nullable = false)
//////    private String description;
//////
//////    @Enumerated(EnumType.STRING)
//////    @Column(nullable = false)
//////    private MedicineStatus status;
//////
//////    @Column(name = "created_at", nullable = false, updatable = false)
//////    private LocalDateTime createdAt;
//////
//////    @Column(name = "updated_at")
//////    private LocalDateTime updatedAt;
//////
//////    @PrePersist
//////    protected void onCreate() {
//////        createdAt = LocalDateTime.now();
//////        updatedAt = LocalDateTime.now();
//////    }
//////
//////    @PreUpdate
//////    protected void onUpdate() {
//////        updatedAt = LocalDateTime.now();
//////    }
//////
//////}
////
////
////package com.pharmaTrace.entity;
////
////import jakarta.persistence.*;
////import lombok.AllArgsConstructor;
////import lombok.Builder;
////import lombok.Data;
////import lombok.NoArgsConstructor;
////
////import java.math.BigDecimal;
////import java.time.LocalDateTime;
////
////@Entity
////@Table(name = "medicines", indexes = {
////        @Index(name = "idx_batch_number", columnList = "batch_number"),
////        @Index(name = "idx_manufacturer_id", columnList = "manufacturer_id"),
////        @Index(name = "idx_qr_code", columnList = "qr_code", unique = true),
////        @Index(name = "idx_status", columnList = "status"),
////        @Index(name = "idx_serial_number", columnList = "serial_number", unique = true),
////        @Index(name = "idx_created_at", columnList = "created_at"),
////
////})
////@Data
////@NoArgsConstructor
////@AllArgsConstructor
////@Builder
////public class Medicine {
////
////    @Id
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
////    private Long id;
////
////    @Column(nullable = false, length = 255)
////    private String name;
////
////    @Column(nullable = false, length = 100)
////    private String batchNumber;
////
////    @Column(nullable = false)
////    private Long manufacturerId;
////
////    // ===== NEW FIELDS =====
////    @Column(nullable = false, precision = 10, scale = 2)
////    @Builder.Default
////    private BigDecimal price = BigDecimal.ZERO;  // Default price to 0 if not provided    private String storageConditions;
////
////
////    @Column(nullable = false, length = 100)
////    private String manufacturer;
////
////    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
////    private String qrCode;
////
////    @Column(nullable = false)
////    private Integer quantity;
////
////    @Column(columnDefinition = "TEXT")
////    private String description;
////
////    @Column(length = 100)
////    private String serialNumber;
////
////    @Column(length = 50)
////    private String expiryDate;
////
////    @Column(length = 50)
////    private String manufacturingDate;
////
////    @Column(length = 500)
////    private String storageConditions;
////
////    @Enumerated(EnumType.STRING)
////    @Column(nullable = false, length = 50)
////    private MedicineStatus status;
////
////    @Column(name = "is_verified")
////    @Builder.Default
////    private Boolean isVerified = false;
////
////    @Column(name = "verification_count")
////    @Builder.Default
////    private Integer verificationCount = 0;
////
////    @Column(name = "last_verified_at")
////    private LocalDateTime lastVerifiedAt;
////
////    @Column(name = "created_at", nullable = false, updatable = false)
////    private LocalDateTime createdAt;
////
////    @Column(name = "updated_at")
////    private LocalDateTime updatedAt;
////
////    @Column(columnDefinition = "TEXT")
////    private String additionalInfo;
////
////    @PrePersist
////    protected void onCreate() {
////        if (createdAt == null) {
////            createdAt = LocalDateTime.now();
////        }
////        if (updatedAt == null) {
////            updatedAt = LocalDateTime.now();
////        }
////    }
////
////    @PreUpdate
////    protected void onUpdate() {
////        updatedAt = LocalDateTime.now();
////    }
////
////}
//
//
//package com.pharmaTrace.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//@Entity
//@Table(name = "medicines", indexes = {
//        @Index(name = "idx_manufacturer_id", columnList = "manufacturer_id"),
//        @Index(name = "idx_batch_number", columnList = "batch_number"),
//        @Index(name = "idx_qr_code", columnList = "qr_code"),
//        @Index(name = "idx_status", columnList = "status")
//})
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Medicine {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false, length = 255)
//    private String name;
//
//    @Column(length = 1000)
//    private String description;
//
//    @Column(nullable = false, length = 50)
//    private String batchNumber;
//
//    @Column(nullable = false)
//    private Long manufacturerId;
//
//    @Column(nullable = false, length = 255)
//    private String manufacturer;
//
//    @Column(nullable = false, length = 500)
//    private String qrCode;
//
//    @Column(nullable = false)
//    private Integer quantity;
//
//    @Column(length = 100)
//    private String serialNumber;
//
//    @Column(nullable = false, length = 50)
//    @Builder.Default
//    private MedicineStatus status = MedicineStatus.CREATED;
//
//    @Column(nullable = false)
//    private LocalDate expiryDate;
//
//    @Column(nullable = false)
//    private LocalDate manufacturingDate;
//
//    @Column(length = 500)
//    private String storageConditions;
//
//    // ===== NEW FIELDS =====
//    @Column(nullable = false, precision = 10, scale = 2)
//    @Builder.Default
//    private BigDecimal price = BigDecimal.ZERO;  // Default price to 0 if not provided
//
//    @Column(length = 255)
//    private String currency;
//
//    @Column(nullable = false)
//    @Builder.Default
//    private Boolean isVerified = false;
//
//    @Column
//    private LocalDateTime lastVerifiedAt;
//
//    @Column
//    @Builder.Default
//    private Integer verificationCount = 0;
//
//    @Column(length = 1000)
//    private String additionalInfo;
//
//    @CreationTimestamp
//    @Column(nullable = false, updatable = false)
//    private LocalDateTime createdAt;
//
//    @UpdateTimestamp
//    @Column(nullable = false)
//    private LocalDateTime updatedAt;
//
//    @PrePersist
//    protected void onCreate() {
//        if (this.price == null) {
//            this.price = BigDecimal.ZERO;
//        }
//        if (this.currency == null) {
//            this.currency = "USD";
//        }
//        if (this.status == null) {
//            this.status = MedicineStatus.CREATED;
//        }
//        if (this.isVerified == null) {
//            this.isVerified = false;
//        }
//        if (this.verificationCount == null) {
//            this.verificationCount = 0;
//        }
//    }
//}

package com.pharmaTrace.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "medicines", indexes = {
        @Index(name = "idx_batch_number", columnList = "batch_number"),
        @Index(name = "idx_manufacturer_id", columnList = "manufacturer_id"),
        @Index(name = "idx_qr_code", columnList = "qr_code"),
        @Index(name = "idx_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 100)
    private String batchNumber;

    @Column(nullable = false)
    private Long manufacturerId;

    @Column(nullable = false, length = 100)
    private String manufacturer;

    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    private String qrCode;

    @Column(nullable = false)
    private Integer quantity;

    @Column(length = 50)
    private String serialNumber;

    @Column(length = 50)
    private String expiryDate;

    @Column(length = 50)
    private String manufacturingDate;

    @Column(length = 500)
    private String storageConditions;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Builder.Default
    private MedicineStatus status = MedicineStatus.CREATED;

    @Column(name = "is_verified")
    @Builder.Default
    private Boolean isVerified = false;

    @Column(name = "verification_count")
    @Builder.Default
    private Integer verificationCount = 0;

    @Column(name = "last_verified_at")
    private LocalDateTime lastVerifiedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(columnDefinition = "TEXT")
    private String additionalInfo;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
        if (status == null) {
            status = MedicineStatus.CREATED;
        }
        if (isVerified == null) {
            isVerified = false;
        }
        if (verificationCount == null) {
            verificationCount = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
//
//    @ManyToOne
//    @JoinColumn(name = "shipment_id")
//    private Shipment shipment;
//
//    private Long Shipment;

//    @Column(name = "medicine_id", nullable = false)
//    private Long medicineId;
}