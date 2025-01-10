# Backend of SENTINEL

The **SENTINEL** backend is the heart of the system, responsible for managing logical operations, communication between microservices and interfacing with IoT devices. It is designed to guarantee efficiency, scalability and robustness, exploiting an architecture based on microservices and advanced technologies such as Mosquitto, CrateDB and MongoDB.

---

## Key features

### **User management**
- **Registration and Authentication**: Secure management of user accounts using advanced authentication protocols.
- **Roles and Permissions**: Assigning specific roles to maintenance managers to ensure controlled access.

### **Machinery Management**
- **Census**: Adding new machinery with detailed data.
- **Operations**: Remote control of machinery (start, stop, maintenance, etc.).
- **Monitoring**: Continuous data collection from machinery via MQTT.

### **Monitoring and anomaly detection**.
- **Real-time data processing**: Analysis of data collected from sensors to detect anomalies in machine behaviour.
- **Notifications**: Automatic generation of notifications in the event of anomalies or critical updates.

### **Integration with frontend and dashboard**.
- **API RESTful**: Smooth communication with frontend and other services via REST endpoints.
- **Integration with Grafana**: Provision of structured analytical data for dashboards.

---

## Architecture

The backend architecture is based on microservices that communicate with each other using **Mosquitto** as an MQTT broker. Each microservice has a specific and independent role, ensuring modularity and scalability.

### **Main Technologies**
- **Spring Boot**: Framework used for the development of microservices.
- **Mosquitto**: MQTT broker for communication between devices and services.
- **CrateDB**: Distributed Database for storing analytical data.
- **MongoDB**: NoSQL database for operational data management.

---

## Prerequisites

- **Java**: Version 21 or higher.
- **Docker** and **Docker Compose**: For running the backend in containerised environments.

---

## Installation and start-up

1. Clone the repository:
   ```bash
   git clone https://github.com/UniSalento-IDALab-IoTCourse-2023-2024/wot-project-2023-2024-Backend-Mirko-Caforio.git
   
   cd AcquisitionService
   docker-compose up -d
   
   cd AuthService
   docker-compose up -d
   
   cd MachineService
   docker-compose up -d
   
   cd NotificationService
   docker-compose up -d
   
   cd UserManagementService
   docker-compose up -d
   ```