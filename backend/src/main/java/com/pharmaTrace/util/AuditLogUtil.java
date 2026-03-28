package com.pharmaTrace.util;


import com.pharmaTrace.entity.AuditLog;
import com.pharmaTrace.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

    @Slf4j
    @Component
    @RequiredArgsConstructor
    public class AuditLogUtil {

        private final AuditLogRepository auditLogRepository;

        public void logAudit(Long userId, String action, String entity, Long entityId, String details, String status) {
            try {
                 AuditLog auditLog = AuditLog.builder()
                        .userId(userId)
                        .action(action)
                        .entity(entity)
                        .entityId(entityId)
                        .details(details)
                        .status(status)
                        .createdAt(LocalDateTime.now())
                        .build();

                auditLogRepository.save(auditLog);
                log.debug("Audit log created: {} - {}", action, entity);
            } catch (Exception ex) {
                log.error("Error creating audit log", ex);
            }
        }
    }

