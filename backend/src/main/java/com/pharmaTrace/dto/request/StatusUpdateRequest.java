package com.pharmaTrace.dto.request;

import com.pharmaTrace.entity.MedicineStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusUpdateRequest {

    @NotNull(message = "Medicine ID is required")
    private Long medicineId;

    @NotNull(message = "Status is required")
    private MedicineStatus status;

    private String location;

    private String remarks;

    private String gpsCoordinates;
}