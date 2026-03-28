package com.pharmaTrace.service;

import com.google.zxing.WriterException;
import com.pharmaTrace.dto.response.QRCodeResponse;
import com.pharmaTrace.entity.Medicine;
import com.pharmaTrace.repository.MedicineRepository;
import com.pharmaTrace.exception.ResourceNotFoundException;
import com.pharmaTrace.util.QRCodeGenerator;
import com.pharmaTrace.util.AuditLogUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class QRCodeService {

    private final MedicineRepository medicineRepository;
    private final QRCodeGenerator qrCodeGenerator;
    private final AuditLogUtil auditLogUtil;

    public QRCodeResponse generateQRCode(Long medicineId, Long manufacturerId) throws IOException, WriterException {
        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found"));

        if (!medicine.getManufacturerId().equals(manufacturerId)) {
            throw new IllegalArgumentException("You don't have permission to generate QR for this medicine");
        }

        String qrCodeUrl = qrCodeGenerator.generateQRCodeImage(medicine.getQrCode());

        auditLogUtil.logAudit(manufacturerId, "GENERATE_QR", "Medicine", medicineId,
                "QR code generated for batch: " + medicine.getBatchNumber(), "SUCCESS");

        log.info("QR code generated for medicine ID: {}", medicineId);

        return QRCodeResponse.builder()
                .medicineId(medicineId)
                .qrCode(medicine.getQrCode())
                .qrCodeImageUrl(qrCodeUrl)
                .batchNumber(medicine.getBatchNumber())
                .success(true)
                .message("QR code generated successfully")
                .build();
    }

    public String verifyQRCode(String qrCode) {
        Medicine medicine = medicineRepository.findByQrCode(qrCode)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid QR code - Medicine not found"));

        return qrCode;
    }
}