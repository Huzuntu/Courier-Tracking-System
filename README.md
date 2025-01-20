# Courier Tracking System

## Overview

This project is a RESTful web application developed in Java Spring Boot that processes streaming geolocation data of couriers. It calculates the total distance traveled by each courier and logs entries when a courier enters a 100-meter radius of any store. The system uses Apache Kafka for real-time data streaming and Docker for infrastructure management.

## Features

- Processes real-time geolocation data of couriers using Kafka.
- Logs courier entries when within a 100-meter radius of specified stores.
- Prevents re-entries to the same store within a 1-minute interval.
- Calculates the total distance traveled by couriers.
- Real-time updates of courier data using Kafka Streams.

## Technologies Used

- Java 17+
- Spring Boot
- Apache Kafka
- Docker
- GeoCalc Library (for distance calculations)
- Spring Kafka (for Kafka integration)
- Kafka Streams (for real-time data processing)

## Project Architecture

The application consists of multiple components working together:

1. **Courier Service**: Handles geolocation data, updates the total distance traveled by couriers, and checks proximity to stores.
2. **Proximity Service**: Monitors courier proximity to stores and logs events using Kafka.
3. **Kafka Infrastructure**:
   - **Producer**: Sends courier geolocation updates to Kafka.
   - **Consumer**: Consumes the courier location data and aggregates it using Kafka Streams.
   - **Topics**:
     - `input`: For courier geolocation data.
     - `output`: For aggregated courier data (total distance, store entries).
   - **Kafka Streams**: Processes real-time data and maintains the state store of aggregated courier data.
4. **Docker Setup**:
   - Kafka, Zookeeper, and Kafka UI are managed using `docker-compose` for ease of development and testing.

## Setup Instructions

1. **Clone the Repository:**
   Clone the repository and navigate to the project directory:
   ```bash
   git clone https://github.com/Huzuntu/ Courier-Tracking-System.git
   cd Courier-Tracking
2. **Docker Setup for Kafka and Zookeeper:**
   Make sure Docker is installed and running on your machine. From the root folder of the project, bring up Kafka, Zookeeper, and KafkaUI services:
   ```bash
   docker-compose up -d
   ```
3. **Run the Spring Boot Application:**
   Ensure that you have Java 17+ and Maven installed. From the root directory of the project, build and run the application:
   ```bash
   mvn clean install
   mvn spring-boot:run
4. **Stop the Services:**
   Once you’re done, you can stop the Kafka, Zookeeper, and KafkaUI services:
   ```bash
   docker-compose down
## Access Kafka UI

1.	Once the Docker containers are running, you can access the Kafka UI at http://localhost:7777.
2.	You can use the Kafka UI to monitor Kafka topics and see logs of courier events being published to the output topic.

## API Endpoints
### Base URL

The base URL for the API is:
- **URL:** `(http://localhost:8084/api/couriers)`

### 1. **Log Courier Location**

- **URL:** `/log`
- **Method:** `POST`
- **Description:** Logs a courier's location by sending it to Kafka for processing.
- **Request Body:**
  - `courierId` (integer): Unique identifier for the courier.
  - `lat` (double): Latitude of the courier’s current location.
  - `lng` (double): Longitude of the courier’s current location.

- **Example Request:**
  ```json
  {
    "courierId": 1,
    "lat": 40.730610,
    "lng": -73.935242,
  }
- **Response:**
A success message indicating the location was logged.
- **Example Response:**
  ```json
  {
  "message": "Location logged successfully"
  }
### 2. **Get Total Travel Distance**
- **URL:** `/distance-travelled/{id}`
- **Method:** `GET`
- **Description:** Retrieves the total distance traveled by a courier based on the courier’s ID.
- **Path Parameter:**
  - `id` (integer): The unique ID of the courier for whom the total distance is to be retrieved.
- **Example Request:**
  ```
  GET http://localhost:8080/api/couriers/distance-travelled/1
- **Response:**
A message with the total distance traveled by the courier.
- **Example Response:**
  ```json
  {
  "message": "Courier 1 has traveled a total distance of 1200.50 meters."
  }
- **Error Response:**
  If the courier is not found in the state store:
  ```json
  {
  "message": "Courier with ID: 1, is not found in the state store"
  }
