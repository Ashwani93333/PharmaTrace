//package com.pharmaTrace.repository;
//
//import com.pharmaTrace.entity.Medicine;
//import com.pharmaTrace.entity.MedicineStatus;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface MedicineRepository extends JpaRepository<Medicine, Long> {
//
//    Optional<Medicine> findByQrCode(String qrCode);
//
//    Optional<Medicine> findBySerialNumber(String serialNumber);
//
//    List<Medicine> findByManufacturerId(Long manufacturerId);
//
//    Page<Medicine> findByManufacturerId(Long manufacturerId, Pageable pageable);
//
//    Page<Medicine> findByStatus(MedicineStatus status, Pageable pageable);
//
//    Page<Medicine> findByNameContainingIgnoreCase(String name, Pageable pageable);
//
//    @Query("SELECT m FROM Medicine m WHERE m.batchNumber = :batchNumber AND m.manufacturerId = :manufacturerId")
//    Optional<Medicine> findByBatchNumberAndManufacturerId(@Param("batchNumber") String batchNumber,
//                                                          @Param("manufacturerId") Long manufacturerId);
//
//    @Query("SELECT COUNT(m) FROM Medicine m WHERE m.status = ?1")
//    Long countByStatus(MedicineStatus status);
//
//    @Query("SELECT m FROM Medicine m WHERE m.status IN (?1) ORDER BY m.createdAt DESC")
//    Page<Medicine> findByStatusIn(List<MedicineStatus> statuses, Pageable pageable);
//
//    @Query("SELECT m FROM Medicine m WHERE m.isVerified = true")
//    Page<Medicine> findVerifiedMedicines(Pageable pageable);
//
//    List<Medicine> findByBatchNumber(String batchNumber);
//}

package com.pharmaTrace.repository;

import com.pharmaTrace.entity.Medicine;
import com.pharmaTrace.entity.MedicineStatus;
import com.pharmaTrace.entity.ShipmentMedicine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    // Find by QR code
    Optional<Medicine> findByQrCode(String qrCode);

    // Find by batch number
    Optional<Medicine> findByBatchNumber(String batchNumber);

    // Find by batch number and manufacturer ID
    @Query("SELECT m FROM Medicine m WHERE m.batchNumber = :batchNumber AND m.manufacturerId = :manufacturerId")
    Optional<Medicine> findByBatchNumberAndManufacturerId(@Param("batchNumber") String batchNumber,
                                                          @Param("manufacturerId") Long manufacturerId);

    // Find all medicines by manufacturer
    @Query("SELECT m FROM Medicine m WHERE m.manufacturerId = :manufacturerId ORDER BY m.createdAt DESC")
    List<Medicine> findByManufacturerId(@Param("manufacturerId") Long manufacturerId);

    // Find medicines by manufacturer paginated
    @Query("SELECT m FROM Medicine m WHERE m.manufacturerId = :manufacturerId ORDER BY m.createdAt DESC")
    Page<Medicine> findByManufacturerId(@Param("manufacturerId") Long manufacturerId, Pageable pageable);

    // Find medicines by status
    List<Medicine> findByStatus(MedicineStatus status);

    // Find medicines by status paginated
    Page<Medicine> findByStatus(MedicineStatus status, Pageable pageable);

    List<Medicine> findTop10ByOrderByCreatedAtDesc();
    long countByIsVerifiedTrue();

    @Query("SELECT COUNT(DISTINCT m.manufacturerId) FROM Medicine m")
    long countDistinctManufacturers();

    @Query("SELECT DISTINCT m.manufacturerId, m.manufacturer FROM Medicine m")
    List<Object[]> findDistinctManufacturerDetails();

    // Search medicines by name
    @Query("SELECT m FROM Medicine m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY m.createdAt DESC")
    List<Medicine> findByNameContainingIgnoreCase(@Param("name") String name);

    // Search medicines by name paginated
    @Query("SELECT m FROM Medicine m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY m.createdAt DESC")
    Page<Medicine> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);

    // Count medicines by status
    @Query("SELECT COUNT(m) FROM Medicine m WHERE m.status = :status")
    Long countByStatus(@Param("status") MedicineStatus status);

    // Count medicines by manufacturer
    @Query("SELECT COUNT(m) FROM Medicine m WHERE m.manufacturerId = :manufacturerId")
    Long countByManufacturerId(@Param("manufacturerId") Long manufacturerId);

//    List<ShipmentMedicine> findByShipment_Id(Long shipmentId);





}