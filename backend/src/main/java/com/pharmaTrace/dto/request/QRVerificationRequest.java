package com.pharmaTrace.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QRVerificationRequest {

    @NotBlank(message = "QR Code is required")
    private String qrCode;

    private String location;

    private String gpsCoordinates;
}