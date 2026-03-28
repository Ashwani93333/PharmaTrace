package com.pharmaTrace.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ShipmentRequest {

    private Long distributorId;

    private LocalDate shipmentDate;

    private List<Long> medicineIds;
}