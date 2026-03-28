package com.pharmaTrace.service;

import com.pharmaTrace.dto.request.ShipmentRequest;
import com.pharmaTrace.dto.response.ShipmentResponse;
import com.pharmaTrace.entity.*;
import com.pharmaTrace.repository.MedicineRepository;
import com.pharmaTrace.repository.ShipmentMedicineRepository;
import com.pharmaTrace.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service

public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final MedicineRepository medicineRepository;
    private final ShipmentMedicineRepository shipmentMedicineRepository;

    public ShipmentResponse createShipment(
            ShipmentRequest request,
            Long manufacturerId
    ) {


        Shipment shipment = Shipment.builder()
                .shipmentNumber("SHIP-" + System.currentTimeMillis())
                .manufacturerId(manufacturerId)
                .distributorId(request.getDistributorId())
                .shipmentDate(request.getShipmentDate())
                .status(ShipmentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        Shipment savedShipment = shipmentRepository.save(shipment);

        List<ShipmentMedicine> shipmentMedicines = new ArrayList<>();

        for (Long medicineId : request.getMedicineIds()) {

            Medicine medicine = medicineRepository.findById(medicineId)
                    .orElseThrow(() -> new RuntimeException("Medicine not found"));

            medicine.setStatus(MedicineStatus.IN_TRANSIT);
            medicineRepository.save(medicine);

            ShipmentMedicine shipmentMedicine = ShipmentMedicine.builder()
                    .shipment(savedShipment)
                    .medicineId(medicineId)
                    .quantity(medicine.getQuantity())
                    .build();

            shipmentMedicines.add(shipmentMedicine);
        }

        shipmentMedicineRepository.saveAll(shipmentMedicines);

        return ShipmentResponse.builder()
                .shipmentId(savedShipment.getId())
                .shipmentNumber(savedShipment.getShipmentNumber())
                .manufacturerId(savedShipment.getManufacturerId())
                .distributorId(savedShipment.getDistributorId())
                .shipmentDate(savedShipment.getShipmentDate())
                .status(savedShipment.getStatus().name())
                .medicineIds(request.getMedicineIds())
                .build();
    }
}
