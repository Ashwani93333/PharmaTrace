package com.pharmaTrace.repository;

import com.pharmaTrace.entity.Role;
import com.pharmaTrace.entity.Shipment;
import com.pharmaTrace.entity.ShipmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    Optional<Shipment> findByShipmentNumber(String shipmentNumber);

    List<Shipment> findByFromUserId(Long fromUserId);

    Page<Shipment> findByFromUserId(Long fromUserId, Pageable pageable);

    List<Shipment> findByToUserId(Long toUserId);

    Page<Shipment> findByToUserId(Long toUserId, Pageable pageable);

    List<Shipment> findByMedicineId(Long medicineId);

    Page<Shipment> findByStatus(ShipmentStatus status, Pageable pageable);

    List<Shipment> findByManufacturerId(Long manufacturerId);

    List<Shipment> findByDistributorId(Long distributorId);

    Long countDistinctDistributorIdByManufacturerId(Long manufacturerId);

    @Query("SELECT s FROM Shipment s WHERE s.fromRole = :fromRole AND s.toRole = :toRole")
    List<Shipment> findByFromRoleAndToRole(@Param("fromRole") Role fromRole,
                                           @Param("toRole") Role toRole);

    @Query("SELECT COUNT(s) FROM Shipment s WHERE s.status = ?1")
    Long countByStatus(ShipmentStatus status);

    Page<Shipment> findByStatusOrderByCreatedAtDesc(ShipmentStatus status, Pageable pageable);
}