//package com.pharmaTrace.repository;
//
//import com.pharmaTrace.entity.Medicine;
//import com.pharmaTrace.entity.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.util.List;
//
//public interface ManufacturerRepository extends JpaRepository<User, Long> {
//    // Custom query methods can be defined here
//
//    @Query("SELECT m FROM Medicine m WHERE m.manufacturer = :manufacturer")
//    List<Medicine> findMedicinesByManufacturer(@Param("manufacturer") String manufacturer);
//
//    @Query("SELECT m FROM Medicine m WHERE m.status = :status")
//    List<Medicine> findMedicinesByStatus(@Param("status") String status);
//
//    @Query("SELECT m FROM Medicine m WHERE m.manufacturer = :manufacturer AND m.status = :status")
//    List<Medicine> findMedicinesByManufacturerAndStatus(@Param("manufacturer") String manufacturer, @Param("status") String status);
//
//}