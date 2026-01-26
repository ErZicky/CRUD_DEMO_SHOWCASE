# Spring Boot CRUD & REST API Demo: an educational Java/Spring project

This is a simple Java/Spring project I created to master the basics of CRUD and REST API development a while back. I've refactored the code for better clarity and added English comments to help others who might need it to understand the core concepts.

### Key Features:

- Full CRUD operations (Create, Read, Update, Delete) for product management.

- Best practice architecture: Follows the 3-layer pattern (Controller, Service, and Repository layers) for better maintainability.

- Global Exception Handling using @RestControllerAdvice for consistent API responses.

- Persistence managed via Spring Data JPA and in memory db.
- Automated Health Monitoring: Real-time health checks and automated "Watcher" to detect downtime or performance degradation.

- Built with Gradle

## Included API Endpoints

All endpoints are reachable under the base path: `/api/products`

| Method | Endpoint | Description | Request Body | Success Code                 |
| :--- | :--- | :--- | :--- |:-----------------------------|
| **GET** | `/api/products` | Retrieve all products | None | `200 OK`                     |
| **GET** | `/api/products/{id}` | Retrieve a product by ID | None | `200 OK`                     |
| **GET** | `/api/products/searchLike/{name}` | Search products by name (partial match) | None | `200 OK`                     |
| **POST** | `/api/products` | Create a new product | JSON Product | `201 Created`                |
| **PUT** | `/api/products/{id}` | Update an existing product | JSON Product | `200 OK`                     |
| **DELETE** | `/api/products` | Delete a product (via object) | JSON Product | `204 No Content`             |
| **DELETE** | `/api/products/{id}` | Delete a product by ID | None | `204 No Content`             |
|**GET** | `/actuator/health` | Perform a manual System health check | None | `200 OK or 503 System error` |

## Monitoring & Downtime Detection
this project includes a simple monitoring layer, ideally to reduce mean time to detection, it's made up of 2 component

### 1. Custom Health Indicator

this component extends Spring Boot Actuator. It can return these problems:

* Status UP: Database responds within 500ms.
* Status DEGRADED: Database is online but responding slowly (latency > Xms).
* Status DOWN: Database is unreachable or empty.

### 2. Automated Health Watcher
The HealthWatcher is a background service that uses @Scheduled tasks to monitor the system every 10 seconds.
It acts as a Watcher that proactively polls the CustomHealthIndicator.
In case the health indicator return anything that isn't an healthy system, it triggers an Alert system (logged to console).

## Custom Exception Handling

This project implements a centralized exception handling to make sure the APIs always returns a readable JSON format, even when errors occur.

### Global Exception Handler
Using `@RestControllerAdvice`, the application intercepts specific exceptions before they reach the client. This approach follows the **Separation of Concerns** principle, to keep the Controller clean from `try-catch` or `if-else`  blocks.

*(Note: Some methods in the controller still include manual error checks to showcase how to manage them without an exception handler for educational purposes).*

### Managed Exceptions

| Exception | HTTP Status | Response Body | Description |
| :--- | :--- | :--- | :--- |
| `ProductNotFoundException` | `404 Not Found` | Custom Error JSON | Thrown when a requested Product ID does not exist in the database. |
| `Exception` (Generic) | `418 I'm a teapot` | Custom Error JSON | A "catch-all" safety net for unexpected errors (e.g., DB connection issues).|

> **N.B:** normally we would use a `500 Internal Server Error` code, in the project the `418 I'm a teapot` is used to showcase that the code is indeed taking our custom exception handler

## Testing

I highly recommend using [Postman](https://www.postman.com/) and connecting to http://localhost:8080/ to test the API endpoints.

### Prerequisites to run
* **Java JDK 17** or higher.
* **Gradle**
  
