package com.pharmaTrace.entity;//package com.pharmaTrace.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "audit_logs", indexes = {
//        @Index(name = "idx_user_id", columnList = "user_id"),
//        @Index(name = "idx_action_timestamp", columnList = "timestamp")
//})
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class AuditLog {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    @Column(nullable = false)
//    private String action;
//
//    @Column(nullable = false)
//    private String entityType;
//
//    @Column(nullable = false)
//    private Long entityId;
//
//    @Column(columnDefinition = "TEXT")
//    private String details;
//
//    @Column(name = "timestamp", nullable = false, updatable = false)
//    private LocalDateTime timestamp;
//
//    @Column(name = "ip_address")
//    private String ipAddress;
//
//    @PrePersist
//    protected void onCreate() {
//        timestamp = LocalDateTime.now();
//    }
//
//}


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs", indexes = {
        @Index(name = "idx_user_id_audit", columnList = "user_id"),
        @Index(name = "idx_action", columnList = "action"),
        @Index(name = "idx_entity", columnList = "entity"),
        @Index(name = "idx_created_at_audit", columnList = "created_at"),
        @Index(name = "idx_user_action", columnList = "user_id, action")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(length = 100)
    private String userName;

    @Column(nullable = false, length = 50)
    private String action;

    @Column(length = 100)
    private String entity;

    @Column
    private Long entityId;

    @Column(columnDefinition = "TEXT")
    private String details;

    @Column(length = 50)
    private String status;

    @Column(length = 45)
    private String ipAddress;

    @Column(length = 100)
    private String userAgent;

    @Column(columnDefinition = "TEXT")
    private String oldValue;

    @Column(columnDefinition = "TEXT")
    private String newValue;

    @Column(length = 50)
    private String entityName;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(length = 50)
    private String changeType; // CREATE, UPDATE, DELETE, VIEW, LOGIN, LOGOUT, etc.

    @Column(columnDefinition = "TEXT")
    private String metadata;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}