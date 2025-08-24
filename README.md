# E-commerce Backend (Personal Project)

A full-stack ready backend system for an E-commerce platform, built with Spring Boot and MySQL. This project is a practice in designing scalable backend architectures for real-world applications.

## Features

### Authentication & Authorization
- Role-based access (Admin, Customer, Supplier)
- JWT authentication with Spring Security

### Product Management
- Categories, Products, Discounts
- Ratings, Feedback, Favorite Items
- Pagination & filtering for web, mobile, and POS

### Order & Invoice Handling
- Order placement, sales, order history
- Invoice management

### Payment
- Multiple payment methods
- Transaction recording

### Chatbot Module
- Chat history storage
- Simple chatbot integration

### System Utilities
- Logging
- Token tracking

## Architecture
- Layered design: Controller → Service → Repository → Database
- DTO pattern for request/response
- Exception handling with custom error responses
- Configurable via `application.yml`

## Testing
- Unit testing with JUnit 5 & Mockito
- Integration testing with Spring Boot Test
- Repository testing using H2 or Testcontainers (MySQL)
- Coverage via JaCoCo

## Technologies
- Spring Boot (Web, Data JPA, Security, Validation)
- MySQL (production), H2 (testing)
- JWT for authentication
- Maven for build & dependency management
- JUnit 5, Mockito, Testcontainers for testing

## Status
- Backend APIs functional & modularized
- Ready for integration with web, mobile, and POS applications
- Future improvements: CI/CD pipeline, API Gateway, and microservices migration
