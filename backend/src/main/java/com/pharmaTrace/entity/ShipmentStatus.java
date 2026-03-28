package com.pharmaTrace.entity;

public enum ShipmentStatus {
    PENDING("Shipment pending - awaiting dispatch"),
    DISPATCHED("Shipment dispatched from source"),
    IN_TRANSIT("Shipment in transit"),
    OUT_FOR_DELIVERY("Out for delivery"),
    DELIVERED("Shipment delivered successfully"),
    REJECTED("Shipment rejected by recipient"),
    RETURNED("Shipment returned to sender"),
    CANCELLED("Shipment cancelled"),
    DAMAGED_IN_TRANSIT("Damaged during transit"),
    LOST("Shipment lost"),
    DELAYED("Shipment delayed");

    private final String description;

    ShipmentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static ShipmentStatus fromString(String status) {
        for (ShipmentStatus shipmentStatus : ShipmentStatus.values()) {
            if (shipmentStatus.name().equalsIgnoreCase(status)) {
                return shipmentStatus;
            }
        }
        throw new IllegalArgumentException("Invalid shipment status: " + status);
    }
}
