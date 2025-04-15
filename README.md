
# 🌿 Air Quality Monitoring API – Coding Challenge

This is a REST API web service for collecting, storing and querying air quality sensor data.

## 📌 Features

- Add new sensor measurements via a REST API.
- Query measurements:
  - Filter by one or more sensor IDs.
  - Filter by metric names (e.g., CO₂, PM2.5).
  - Apply statistics like **min**, **max**, **sum**, or **avg** per metric.
  - Optional date range (`from`, `to`). If not specified, returns the most recent data.

## 🛠️ Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- H2 Database (in-memory)
- Maven

## 📫 API Endpoints

### Access the API at:

```
http://localhost:8080
```

### Add a new measurement



```http
POST /measurements
```

#### Request Body

```json
{
  "sensorId": 1,
  "capturedOn": "2025-04-12T14:30:00",
  "metrics": [
    {"name": "CO2", "value": 12.5},
    {"name": "PM2.5", "value": 20.0}
  ]
}
```

---

### Get measurements (most recent)

```http
GET /measurements?sensorIds=1,2&metrics=CO2,PM2.5
```

Returns latest measurement(s) for given sensors and metric names.

---

### Get measurements with statistics

```http
GET /measurements?sensorIds=1&metrics=CO2,PM2.5&stat=avg&from=2025-04-01T00:00:00&to=2025-04-07T23:59:59
```

Returns average of selected metrics for sensor `1` over the specified date range.

---

## 🧪 Sample Queries

- Get measurement of **benzene** for sensor 1:
  ```
  GET /measurements?sensorIds=1&metrics=Benzene
  ```

- Get average value of **CO₂** and **PM2.5** for sensor 2 over the last week:
  ```
  GET /measurements/stats?sensorIds=2&metrics=CO2,PM2.5&stat=avg&from=2025-04-07T00:00:00&to=2025-04-14T00:00:00
  ```
---

## 🛑 Error Handling

- `404 NOT FOUND`: Sensor not found
- `400 BAD REQUEST`: Invalid or missing parameters

---

## 🗂️ Technical Notes & Design

- 📦 **Data Model**:  
  `Sensor` → `Measurement` → `Metric`
- 🧠 **Business Logic**:  
  Handled in `MeasurementService` with filtering/aggregation
- ✅ **Validation**:  
  Done using `@NotNull`, `@NotEmpty`, and `@Valid` annotations along with `@RestControllerAdvice`
- 🧪 **Testing**:  
  Unit tests with JUnit + Mockito for services and exception handling

---

## ✅ Done

- Basic REST endpoints for posting/querying measurements
- In-memory persistence using H2
- Input validation & exception management
- Unit tests for main business logic

---

## 🗒️ Notes

- This is just a **proof of concept**, built within only  few hours.
- Feel free to improve or expand it.

