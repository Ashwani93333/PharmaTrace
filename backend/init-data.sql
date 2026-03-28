-- Insert Admin User
INSERT INTO users (name, email, password, role, is_active, created_at, updated_at, organization_name)
VALUES ('Admin User', 'admin@mediatrace.com', '$2a$10$nOQZYvEYYZYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY', 'ADMIN', true, NOW(), NOW(), 'MediTrace Admin');

-- Insert Manufacturer
INSERT INTO users (name, email, password, role, is_active, created_at, updated_at, organization_name)
VALUES ('Pharma Corp', 'pharma@mediatrace.com', '$2a$10$nOQZYvEYYZYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY', 'MANUFACTURER', true, NOW(), NOW(), 'Pharma Corporation Ltd.');

-- Insert Distributor
INSERT INTO users (name, email, password, role, is_active, created_at, updated_at, organization_name)
VALUES ('Logistics Inc', 'dist@mediatrace.com', '$2a$10$nOQZYvEYYZYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY', 'DISTRIBUTOR', true, NOW(), NOW(), 'Logistics Inc');

-- Insert Pharmacy
INSERT INTO users (name, email, password, role, is_active, created_at, updated_at, organization_name)
VALUES ('Health Pharmacy', 'pharmacy@mediatrace.com', '$2a$10$nOQZYvEYYZYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY', 'PHARMACY', true, NOW(), NOW(), 'Health Pharmacy Plus');

-- Sample Medicine
INSERT INTO medicines (name, description, batch_number, manufacturer_id, manufacturer, qr_code, status, serial_number, expiry_date, manufacturing_date, quantity, storage_conditions, is_verified, verification_count, created_at, updated_at)
VALUES ('Aspirin 500mg', 'Aspirin tablets for pain relief', 'BATCH-2024-001', 2, 'Pharma Corp', 'QR-UNIQUE-HASH-001', 'CREATED', 'SN-001', '2026-12-31', '2024-03-25', 1000, 'Room temperature', false, 0, NOW(), NOW());