package com.pharmaTrace.repository;

import com.pharmaTrace.entity.TrackingLog;
import com.pharmaTrace.entity.MedicineStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TrackingLogRepository extends JpaRepository<TrackingLog, Long> {

    List<TrackingLog> findByMedicineIdOrderByCreatedAtAsc(Long medicineId);

    Page<TrackingLog> findByMedicineId(Long medicineId, Pageable pageable);

    List<TrackingLog> findByStatus(MedicineStatus status);

    List<TrackingLog> findByUpdatedBy(Long userId);

    @Query("SELECT t FROM TrackingLog t WHERE t.medicineId = :medicineId ORDER BY t.createdAt DESC LIMIT 1")
    TrackingLog findLatestTrackingLogForMedicine(@Param("medicineId") Long medicineId);

    @Query("SELECT t FROM TrackingLog t WHERE t.createdAt BETWEEN :startDate AND :endDate")
    List<TrackingLog> findTrackingLogsBetweenDates(@Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(t) FROM TrackingLog t WHERE t.medicineId = :medicineId")
    Long countByMedicineId(@Param("medicineId") Long medicineId);

    Page<TrackingLog> findByUpdatedByOrderByCreatedAtDesc(Long userId, Pageable pageable);
}