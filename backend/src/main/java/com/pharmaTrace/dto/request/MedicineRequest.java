////package com.pharmaTrace.dto.request;
////
////import jakarta.persistence.Column;
////import jakarta.validation.constraints.*;
////import lombok.AllArgsConstructor;
////import lombok.Builder;
////import lombok.Data;
////import lombok.NoArgsConstructor;
////
////import java.math.BigDecimal;
////
////@Builder
////@Data
////@NoArgsConstructor
////@AllArgsConstructor
////public class MedicineRequest {
////
////    @NotBlank(message = "Medicine name is required")
////    private String name;
////
////    @NotBlank(message = "description number is required")
////    private String description;
////
////    @NotBlank(message = "Batch number is required")
////    private String batchNumber;
////
////    @NotBlank(message = "Serial number is required")
////    private String serialNumber;
////
////    @NotBlank(message = "Expiry date is required")
////    private String expiryDate;
////
////    @NotBlank(message = "Manufacturing date is required")
////    private String manufacturingDate;
////
////    @NotNull(message = "Quantity is required")
////    @Positive(message = "Quantity must be greater than 0")
////    private Integer quantity;
////
////    // ===== NEW FIELDS =====
////    @NotNull(message = "Price is required")
////    @DecimalMin(value = "0.0", inclusive = true, message = "Price cannot be negative")
////    @DecimalMax(value = "999999.99", message = "Price cannot exceed 999,999.99")
////    @Builder.Default
////    private BigDecimal price = BigDecimal.ZERO;
////
////
////    private String additionalInfo;
////}
//
//
//package com.pharmaTrace.dto.request;
//
//import jakarta.validation.constraints.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class MedicineRequest {
//
//    @NotBlank(message = "Medicine name is required")
//    @Size(min = 2, max = 255, message = "Name must be between 2 and 255 characters")
//    private String name;
//
//    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
//    private String description;
//
//    @NotBlank(message = "Batch number is required")
//    @Size(min = 5, max = 50, message = "Batch number must be between 5 and 50 characters")
//    private String batchNumber;
//
//    @NotBlank(message = "Serial number is required")
//    @Size(min = 3, max = 100, message = "Serial number must be between 3 and 100 characters")
//    private String serialNumber;
//
//    @NotNull(message = "Expiry date is required")
//    @FutureOrPresent(message = "Expiry date must be in the future")
//    private LocalDate expiryDate;
//
//    @NotNull(message = "Manufacturing date is required")
//    @PastOrPresent(message = "Manufacturing date must be in the past or present")
//    private LocalDate manufacturingDate;
//
//    @NotNull(message = "Quantity is required")
//    @Min(value = 1, message = "Quantity must be at least 1")
//    @Max(value = 1000000, message = "Quantity cannot exceed 1,000,000")
//    private Integer quantity;
//
//    @Size(max = 500, message = "Storage conditions cannot exceed 500 characters")
//    private String storageConditions;
//
//    // ===== NEW FIELDS =====
//    @NotNull(message = "Price is required")
//    @DecimalMin(value = "0.0", inclusive = true, message = "Price cannot be negative")
//    @DecimalMax(value = "999999.99", message = "Price cannot exceed 999,999.99")
//    @Builder.Default
//    private BigDecimal price = BigDecimal.ZERO;
//
//    @Size(max = 10, message = "Currency code must not exceed 10 characters")
//    @Builder.Default
//    private String currency = "USD";
//
//    @Size(max = 1000, message = "Additional info cannot exceed 1000 characters")
//    private String additionalInfo;
//}

package com.pharmaTrace.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineRequest {

    @NotBlank(message = "Medicine name is required")
    @Size(min = 2, max = 255, message = "Name must be between 2 and 255 characters")
    private String name;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotBlank(message = "Batch number is required")
    @Size(min = 5, max = 100, message = "Batch number must be between 5 and 100 characters")
    private String batchNumber;

    @NotBlank(message = "Serial number is required")
    @Size(min = 3, max = 100, message = "Serial number must be between 3 and 100 characters")
    private String serialNumber;

    @NotBlank(message = "Expiry date is required")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Expiry date must be in format YYYY-MM-DD")
    private String expiryDate;

    @NotBlank(message = "Manufacturing date is required")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Manufacturing date must be in format YYYY-MM-DD")
    private String manufacturingDate;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 1000000, message = "Quantity cannot exceed 1,000,000")
    private Integer quantity;

    @Size(max = 500, message = "Storage conditions cannot exceed 500 characters")
    private String storageConditions;

    @Size(max = 1000, message = "Additional info cannot exceed 1000 characters")
    private String additionalInfo;
}