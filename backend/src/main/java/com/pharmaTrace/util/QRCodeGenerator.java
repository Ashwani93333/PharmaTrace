package com.pharmaTrace.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Slf4j
@Component
public class QRCodeGenerator {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;

    public String generateQRCodeImage(String text) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, WIDTH, HEIGHT);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

        byte[] pngData = pngOutputStream.toByteArray();
        String base64Image = Base64.getEncoder().encodeToString(pngData);

        log.info("QR code image generated successfully");
        return "data:image/png;base64," + base64Image;
    }

    public boolean validateQRCodeFormat(String qrCode) {
        return qrCode != null && !qrCode.isEmpty() && qrCode.length() >= 20;
    }
}