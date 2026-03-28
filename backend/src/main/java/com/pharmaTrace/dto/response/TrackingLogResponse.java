package com.pharmaTrace.dto.response;



import com.pharmaTrace.entity.MedicineStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackingLogResponse {

    private Long id;

    private Long medicineId;

    private MedicineStatus status;

    private Long updatedBy;

    private String updatedByName;

    private String updatedByRole;

    private LocalDateTime createdAt;

    private String remarks;

    private String location;

    private String gpsCoordinates;

    private String metadata;
}
