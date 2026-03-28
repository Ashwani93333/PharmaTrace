package com.pharmaTrace.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplyChainHistoryResponse {

    private MedicineResponse medicine;

    private List<TrackingLogResponse> trackingHistory;

    private Boolean isAuthentic;

    private Integer verificationCount;

    private String currentStatus;

    private String message;
}