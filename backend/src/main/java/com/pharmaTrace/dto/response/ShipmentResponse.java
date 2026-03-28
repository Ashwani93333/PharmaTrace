package com.pharmaTrace.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ShipmentResponse {

    private Long shipmentId;
    private String shipmentNumber;
    private Long manufacturerId;
    private Long distributorId;
    private LocalDate shipmentDate;
    private String status;
    private List<Long> medicineIds;
}