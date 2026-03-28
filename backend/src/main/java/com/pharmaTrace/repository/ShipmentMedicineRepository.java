package com.pharmaTrace.repository;

import com.pharmaTrace.entity.Medicine;
import com.pharmaTrace.entity.ShipmentMedicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShipmentMedicineRepository extends JpaRepository<ShipmentMedicine, Long> {

    List<ShipmentMedicine> findByShipmentId(Long shipmentId);
}