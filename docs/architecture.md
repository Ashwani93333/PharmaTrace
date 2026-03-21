📄 💊 PHARMATRACE – SYSTEM ARCHITECTURE DOCUMENTATION
🧠 1. Overview

PHARMATRACE is an AI + Blockchain-powered system designed to ensure end-to-end transparency, authenticity, and security in the pharmaceutical supply chain.

It creates a Digital Twin of every drug and tracks it from manufacturer → distributor → pharmacy → patient.

🎯 2. Objectives
Eliminate counterfeit medicines
Ensure real-time traceability
Provide tamper-proof transaction logs
Enable instant drug verification
Detect anomalies using AI
🏗️ 3. High-Level Architecture
[ Frontend (Web UI) ]
↓
[ Spring Boot Backend (REST APIs) ]
↓
[ PostgreSQL Database ]
↓
[ AI Microservice (Anomaly Detection) ]
↓
[ Blockchain Layer (Immutable Ledger) ]
🧩 4. System Components
🎨 4.1 Frontend Layer
Tech:
HTML, CSS, JavaScript
Responsibilities:
User interface for all roles
QR scanning & verification
Dashboard visualization
Alerts display
Key Modules:
Dashboard UI
QR Scanner
Alert Panel
Role-based views
⚙️ 4.2 Backend Layer (Spring Boot)
Responsibilities:
Business logic
API handling
Security & authentication
Integration with AI & Blockchain
Core Modules:
🔐 Auth Module
JWT-based authentication
Role-based access control
💊 Drug Module
Create drug batches
Generate QR codes
Store metadata
🚚 Tracking Module
Update shipment location
Maintain lifecycle logs
🔔 Alert Module
Trigger real-time alerts
Notify stakeholders
🔗 Blockchain Module
Log transactions immutably
Verify integrity
🤖 AI Module
Detect anomalies
Generate risk score
🗄️ 4.3 Database Layer (PostgreSQL)
Stores:
Users
Drug batches
Shipment data
Transaction logs
Key Tables:
users
drug
shipment
transaction_log
🤖 4.4 AI Microservice
Tech:
Python (Flask)
Responsibilities:
Analyze supply chain data
Detect anomalies
Assign risk scores
Logic:
Temperature threshold violation
Route deviation
Delay detection
🔗 4.5 Blockchain Layer
Tech:
Ethereum / Hardhat
Responsibilities:
Store immutable transaction logs
Verify drug authenticity
Stored Data:
Drug ID
Owner
Timestamp
Hash
🔄 5. Data Flow
📦 Drug Lifecycle Flow
Manufacturer creates drug batch
QR code is generated
Data stored in database + blockchain
Distributor updates shipment
AI evaluates risk
Alerts triggered if anomaly detected
Pharmacy verifies drug
Patient scans QR for authenticity
🔐 6. Security Architecture
JWT Authentication
Role-based authorization
SHA-256 hashing
Blockchain immutability
Secure API endpoints
🚀 7. Core Features Implementation
🔍 QR-Based Tracking
Each drug has unique QR
Encodes batch ID & hash
Used for verification
🔐 Cryptographic Hashing
SHA-256 used
Ensures data integrity
Detects tampering
🤖 AI Anomaly Detection
Rule-based + ML-ready system
Calculates risk score
Flags suspicious activities
🔔 Real-Time Alerts
Triggered on high risk
Sent to admin/distributor/pharmacy
🔗 Blockchain Logging
Immutable transaction record
Ensures trust & transparency
📊 8. Risk Scoring System
Condition	Score
Temperature violation	+30
Delay in shipment	+20
Unknown location	+40
⚙️ 9. API Architecture
REST APIs
Method	Endpoint	Description
POST	/api/auth/login	Login
POST	/api/drug/create	Create drug
GET	/api/drug/{id}	Get details
POST	/api/track/update	Update tracking
GET	/api/verify/{qr}	Verify drug
📦 10. Deployment Architecture
Components:
Backend (Spring Boot)
Frontend (Static hosting)
AI service (Flask server)
Blockchain node
PostgreSQL DB
📈 11. Scalability Design
Microservice-ready architecture
AI service decoupled
Blockchain as trust layer
Horizontal scaling possible
🔮 12. Future Enhancements
IoT sensor integration
Predictive analytics
Mobile application
Government integration
Advanced ML models
🏆 13. Key Innovations
Digital Twin of medicines
AI + Blockchain integration
Real-time risk detection
End-to-end traceability