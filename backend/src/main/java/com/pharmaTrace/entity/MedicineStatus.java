package com.pharmaTrace.entity;

public enum MedicineStatus {
    CREATED,
    SENT_TO_DISTRIBUTOR,
    RECEIVED_BY_DISTRIBUTOR,
    SENT_TO_PHARMACY,
    RECEIVED_BY_PHARMACY,
    SOLD,
    DAMAGED,
    IN_TRANSIT,
    RECALLED
}
