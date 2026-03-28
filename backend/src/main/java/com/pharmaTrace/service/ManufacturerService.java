//package com.pharmatrace.service;
//
//
//import com.pharmaTrace.entity.Medicine;
//import com.pharmaTrace.entity.MedicineStatus;
//import com.pharmaTrace.entity.TrackingLog;
//import com.pharmaTrace.entity.User;
//import com.pharmaTrace.repository.ManufacturerRepository;
//import com.pharmaTrace.repository.MedicineRepository;
//import com.pharmaTrace.repository.TrackingLogRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//public class ManufacturerService {
//
//    @Autowired
//    private ManufacturerRepository manufacturerRepository;
//
//    @Autowired
//    private MedicineRepository medicineRepository;
//
//    @Autowired
//    private TrackingLogRepository trackingRepository;
//
//    public Map<String, Object> getManufacturerDashboard(Long manufacturerId) {
//        Map<String, Object> dashboard = new HashMap<>();
//
//        User manufacturer = manufacturerRepository.findById(manufacturerId)
//                .orElseThrow(() -> new RuntimeException("Manufacturer not found"));
//
//        dashboard.put("manufacturerName", manufacturer.getName());
//        dashboard.put("manufacturerId", manufacturerId);
//        dashboard.put("manufacturerDetails", manufacturer);
//
//        // Get total medicines count
//        List<Medicine> medicines = medicineRepository.findByManufacturerId(manufacturerId);
//        dashboard.put("totalMedicines", medicines.size());
//
//        // Get medicines by status
//        Map<MedicineStatus, Long> medicinesByStatus = medicines.stream()
//                .collect(Collectors.groupingBy(Medicine::getStatus, Collectors.counting()));
//        dashboard.put("medicinesByStatus", medicinesByStatus);
//
//        // Get all medicines with status
//        dashboard.put("medicines", medicines.stream()
//                .map(m -> Map.of(
//                        "id", m.getId(),
//                        "name", m.getName(),
//                        "status", m.getStatus(),
//                        "batchNumber", m.getBatchNumber(),
//                        "quantity", m.getQuantity(),
//                        "expiryDate", m.getExpiryDate() != null ? m.getExpiryDate().toString() : ""
//                ))
//                .collect(Collectors.toList()));
//
//        // Get orders from all distributors
//        List<TrackingLog> allOrders = manufacturerRepository.findMedicinesByManufacturer(manufacturerId);
//        dashboard.put("totalOrders", allOrders.size());
//
//
//        // Get orders by status
//        Map<String, Long> ordersByStatus = allOrders.stream()
//                .collect(Collectors.groupingBy(Order::getStatus, Collectors.counting()));
//        dashboard.put("ordersByStatus", ordersByStatus);
//
//        // Get distributor wise orders with acceptance status
//        List<Map<String, Object>> distributorOrders = allOrders.stream()
//                .collect(Collectors.groupingBy(Order::getDistributorId))
//                .entrySet().stream()
//                .map(entry -> Map.of(
//                        "distributorId", entry.getKey(),
//                        "orders", entry.getValue().stream()
//                                .map(o -> Map.of(
//                                        "orderId", o.getId(),
//                                        "status", o.getStatus(),
//                                        "accepted", o.isAccepted(),
//                                        "medicineCount", o.getMedicines().size(),
//                                        "orderDate", o.getOrderDate() != null ? o.getOrderDate().toString() : ""
//                                ))
//                                .collect(Collectors.toList())
//                ))
//                .collect(Collectors.toList());
//
//        dashboard.put("distributorOrders", distributorOrders);
//
//        return dashboard;
//    }
//
//    public Map<String, Long> getMedicineStatusSummary(Long manufacturerId) {
//        List<Medicine> medicines = medicineRepository.findByManufacturerId(manufacturerId);
//        return medicines.stream()
//                .collect(Collectors.groupingBy(Medicine::getStatus, Collectors.counting()));
//    }
//
//    public Map<String, Long> getOrderStatusSummary(Long manufacturerId) {
//        List<Order> orders = orderRepository.findByManufacturerId(manufacturerId);
//        return orders.stream()
//                .collect(Collectors.groupingBy(Order::getStatus, Collectors.counting()));
//    }
//
//    public List<Map<String, Object>> getDistributorOrderStatus(Long manufacturerId) {
//        List<Order> orders = orderRepository.findByManufacturerId(manufacturerId);
//        return orders.stream()
//                .collect(Collectors.groupingBy(Order::getDistributorId))
//                .entrySet().stream()
//                .map(entry -> {
//                    long acceptedCount = entry.getValue().stream()
//                            .filter(Order::isAccepted)
//                            .count();
//                    long pendingCount = entry.getValue().stream()
//                            .filter(o -> !o.isAccepted())
//                            .count();
//
//                    return Map.of(
//                            "distributorId", entry.getKey(),
//                            "totalOrders", (long) entry.getValue().size(),
//                            "acceptedOrders", acceptedCount,
//                            "pendingOrders", pendingCount,
//                            "acceptanceRate", entry.getValue().size() > 0 ?
//                                    (double) acceptedCount / entry.getValue().size() * 100 : 0.0
//                    );
//                })
//                .collect(Collectors.toList());
//    }
//
//    public List<Medicine> getTotalMedicines(Long manufacturerId) {
//        return medicineRepository.findByManufacturerId(manufacturerId);
//    }
//}