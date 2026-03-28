//package com.pharmaTrace.dto.response;
//
//import com.pharmaTrace.entity.Medicine;
//import com.pharmaTrace.entity.MedicineStatus;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class MedicineResponse {
//
//    private Long id;
//
//    private String name;
//
//    private String description;
//
//    private BigDecimal price;
//
//    private String batchNumber;
//
//    private Long manufacturerId;
//
//    private String manufacturer;
//
//    private String serialNumber;
//
//    private LocalDate expiryDate;
//
//    private LocalDate manufacturingDate;
//
//    private Integer quantity;
//
//    private String storageConditions;
//
//    private MedicineStatus status;
//
//    private String qrCode;
//
//    private Boolean isVerified;
//
//    private Integer verificationCount;
//
//    private LocalDateTime lastVerifiedAt;
//
//    private LocalDateTime createdAt;
//
//    private LocalDateTime updatedAt;
//
////    private MedicineResponse convertToMedicineResponse(Medicine medicine) {
////        return MedicineResponse.builder()
////                .id(medicine.getId())
////                .name(medicine.getName())
////                .manufacturer(medicine.getManufacturer())
////                .price(medicine.getPrice())
////                .expiryDate(medicine.getExpiryDate())
////                .build();
////    }
//}

package com.pharmaTrace.dto.response;

import com.pharmaTrace.entity.MedicineStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineResponse {

    private Long id;

    private String name;

    private String description;

    @JsonProperty("batch_number")
    private String batchNumber;

    @JsonProperty("serial_number")
    private String serialNumber;

    @JsonProperty("manufacturer_id")
    private Long manufacturerId;

    private String manufacturer;

    private Integer quantity;

    @JsonProperty("expiry_date")
    private String expiryDate;

    @JsonProperty("manufacturing_date")
    private String manufacturingDate;

    @JsonProperty("storage_conditions")
    private String storageConditions;

    @JsonProperty("qr_code")
    private String qrCode;

    private MedicineStatus status;

    @JsonProperty("is_verified")
    private Boolean isVerified;

    @JsonProperty("verification_count")
    private Integer verificationCount;

    @JsonProperty("last_verified_at")
    private LocalDateTime lastVerifiedAt;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("additional_info")
    private String additionalInfo;
}