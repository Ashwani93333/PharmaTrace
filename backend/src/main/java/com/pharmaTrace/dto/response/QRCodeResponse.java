package com.pharmaTrace.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QRCodeResponse {

    private Long medicineId;
    private String qrCode;
    private String qrCodeImageUrl;
    private String batchNumber;
    private boolean success;
    private String message;
}