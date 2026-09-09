# Payment Platform

Payment Platform is a modular payment processing platform built using Spring Boot microservices. It separates payment validation, core payment processing, and Payment Service Provider (PSP) integrations into independent services.

## Current Services

- **Payment Validation Service** — Validates incoming payment requests before they enter the payment processing flow.
- **Payment Processing Service** — Manages the core payment workflow, payment status, and provider selection.
- **Stripe Provider Service** — Handles Stripe-specific integration and communication with Stripe.
- **PayPal Provider Service** — Handles PayPal-specific integration and communication with PayPal.

## Architecture

The current payment flow is:

```text
                    Merchant System
                          |
                          | Payment Request
                          v
              +-------------------------+
              | Payment Validation      |
              | Service                 |
              +-------------------------+
                          |
                    Validation
                     /       \
                  Invalid     Valid
                    |           |
                    v           v
             Error Response  +-------------------------+
                             | Payment Processing      |
                             | Service                 |
                             +-------------------------+
                                        |
                                  Provider Selection
                                   /             \
                                  /               \
                                 v                 v
                       +----------------+  +----------------+
                       | Stripe Provider|  | PayPal Provider|
                       | Service        |  | Service        |
                       +----------------+  +----------------+
                              |                  |
                              v                  v
                         Stripe API          PayPal API
                              |                  |
                              +--------+---------+
                                       |
                                       v
                              Payment Processing
                                  Service
                                       |
                                       v
                                Merchant System
```

### Responsibility Separation

**Payment Validation Service**

- Validates mandatory fields and request format.
- Validates payment amount, currency, merchant information, and business rules.
- Rejects invalid requests before payment processing.
- Does not contain Stripe- or PayPal-specific integration logic.

**Payment Processing Service**

- Owns the core payment processing workflow.
- Manages payment state and status.
- Determines which provider should process the payment.
- Routes the request to the appropriate provider service.
- Handles provider-independent payment processing logic.
- Processes provider responses and updates payment status.

**Stripe Provider Service**

- Handles all Stripe-specific integration.
- Creates Stripe-specific requests.
- Manages Stripe authentication and API communication.
- Handles Stripe responses and provider-specific errors.
- Converts Stripe responses into platform-standard responses.
- Handles Stripe webhooks/events where required.

**PayPal Provider Service**

- Handles all PayPal-specific integration.
- Creates PayPal-specific requests.
- Manages PayPal authentication and API communication.
- Handles PayPal responses and provider-specific errors.
- Converts PayPal responses into platform-standard responses.
- Handles PayPal webhooks/events where required.

### Core Architecture Principle

> **Payment Processing Service decides which provider should process the payment; the Provider Service knows how to communicate with that provider.**

For example:

```text
Payment Processing Service
          |
          | Use Stripe
          v
Stripe Provider Service
          |
          | Stripe-specific API request
          v
        Stripe
```

or:

```text
Payment Processing Service
          |
          | Use PayPal
          v
PayPal Provider Service
          |
          | PayPal-specific API request
          v
        PayPal
```

This keeps provider-specific complexity isolated from the core payment processing logic.

## Repository Structure

```text
payment-platform/
│
├── payment-validation-service/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   └── resources/
│   │   └── test/
│   └── build.gradle
│
├── payment-processing-service/
│   ├── src/
│   └── build.gradle
│
├── stripe-provider-service/
│   ├── src/
│   └── build.gradle
│
├── paypal-provider-service/
│   ├── src/
│   └── build.gradle
│
├── docs/
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
└── README.md
```

This repository uses a **monorepo** approach. All payment microservices are maintained in a single Git repository while remaining independently buildable and deployable.

## Technology Stack

### Backend

- Java
- Spring Boot
- Spring Web
- Spring Validation
- Spring Boot Actuator
- Gradle
- REST APIs

### Payment Providers

- Stripe
- PayPal

### Testing

- JUnit
- Mockito
- Spring Boot Test
- Postman
- API Automation

### Monitoring

- Spring Boot Actuator
- Health Checks
- Application Metrics
- Logging

### Gradle Dependencies

Dependencies are defined centrally in `gradle/libs.versions.toml` and shared by all services.

| Dependency | Purpose |
|---|---|
| Spring Boot Web MVC | Provides REST controllers and embedded HTTP server support. |
| Spring Boot Validation | Validates request fields and API input constraints. |
| Spring Boot Actuator | Provides operational health and monitoring endpoints. |
| Spring Boot Test | Provides Spring and web-layer testing utilities. |
| Spring Boot DevTools | Provides development-time restart and live reload support. |
| Lombok | Generates common Java boilerplate at compile time. |
| JUnit Platform Launcher | Discovers and runs tests on the JUnit Platform. |

The shared Gradle plugins provide Spring Boot packaging and centralized dependency management.

## Local Development

Each service runs independently and should use a separate port.

Example:

| Service | Port |
|---|---:|
| Payment Validation Service | `8081` |
| Payment Processing Service | `8082` |
| Stripe Provider Service | `8083` |
| PayPal Provider Service | `8084` |

The ports can be changed through the respective Spring Boot configuration.

## Build

Build the complete platform:

```bash
./gradlew clean build
```

Build an individual service:

```bash
./gradlew :payment-validation-service:build
./gradlew :payment-processing-service:build
./gradlew :stripe-provider-service:build
./gradlew :paypal-provider-service:build
```

## Run Services

Start Payment Validation Service:

```bash
./gradlew :payment-validation-service:bootRun
```

Start Payment Processing Service:

```bash
./gradlew :payment-processing-service:bootRun
```

Start Stripe Provider Service:

```bash
./gradlew :stripe-provider-service:bootRun
```

Start PayPal Provider Service:

```bash
./gradlew :paypal-provider-service:bootRun
```

## Configuration

Each service can maintain environment-specific configuration using Spring Boot profiles:

```text
application.yml
application-local.yml
application-test.yml
application-prod.yml
```

Sensitive values must not be committed to Git.

Each service exposes a health endpoint at `/actuator/health`. Only the health endpoint is exposed by default.

Examples include:

- Stripe secret key
- PayPal client ID
- PayPal client secret
- Database credentials
- API tokens
- Authentication secrets

Use environment variables or a secure secrets-management solution.

## API Design

The platform follows REST API principles and uses versioned endpoints.

Example:

```text
POST /api/v1/payments
GET  /api/v1/payments/{paymentId}
POST /api/v1/payments/{paymentId}/refund
```

Common HTTP status codes:

```text
200 OK
201 Created
400 Bad Request
401 Unauthorized
403 Forbidden
404 Not Found
409 Conflict
500 Internal Server Error
```

## Error Handling

Services should return consistent, platform-standard error responses.

Example:

```json
{
  "timestamp": "2026-08-23T20:30:00Z",
  "status": 400,
  "error": "VALIDATION_ERROR",
  "message": "Invalid payment amount",
  "path": "/api/v1/payments"
}
```

Provider-specific errors should be translated by the Provider Service into a standard response before being returned to the Payment Processing Service.

## Health Checks

Each service should expose a health endpoint using Spring Boot Actuator.

```http
GET /actuator/health
```

Example:

```json
{
  "status": "UP"
}
```

Health checks can be used for local development, CI/CD, deployment verification, and monitoring.

## Security and PCI DSS

The platform should follow secure payment-processing practices, including:

- HTTPS for communication.
- Secure API authentication.
- Input validation.
- Secure secret management.
- Sensitive-data masking.
- Secure logging.
- Provider authentication.
- Webhook signature verification.
- Avoiding unnecessary storage or logging of sensitive payment information.

The platform should be designed with PCI DSS requirements in mind. Where possible, sensitive cardholder information should be handled by the appropriate PSP using its recommended integration approach.

The exact PCI DSS scope depends on the payment flow, data handled, integration model, and deployment architecture.

## Testing

Testing is maintained in a separate repository:

```text
payment-platform-test-suite
```

The test repository is intended to provide common testing and automation for all Payment Platform services.

Potential testing areas include:

```text
Unit Testing
Integration Testing
API Testing
End-to-End Testing
Performance Testing
Regression Testing
```

Tools may include:

- JUnit
- Mockito
- Spring Boot Test
- Postman
- Playwright
- Selenium
- JMeter

## Development Guidelines

- Use constructor-based dependency injection.
- Keep controllers thin.
- Keep business logic in service classes.
- Use DTOs for API contracts.
- Validate incoming requests.
- Use centralized exception handling.
- Keep provider-specific logic inside Provider Services.
- Do not expose external PSP response structures directly to internal services.
- Use meaningful logging.
- Never log secrets or sensitive payment information.
- Add unit and integration tests for new functionality.

Recommended package structure:

```text
controller/
service/
repository/
client/
dto/
entity/
exception/
config/
mapper/
```

Provider-specific implementation can additionally be organized under:

```text
stripe/
paypal/
```

## Future Enhancements

The platform can be extended with additional capabilities such as:

- Additional PSP integrations such as Adyen.
- Payment retries.
- Refund processing.
- Payment cancellation.
- Payment reconciliation.
- Settlement processing.
- Provider routing and failover.
- Fraud detection.
- Transaction reporting.
- Webhook/event processing.
- Apache Kafka for asynchronous communication.
- Distributed tracing.
- Centralized logging.
- Docker and Kubernetes deployment.
- CI/CD automation.

## Project Goals

The primary goals of the Payment Platform are:

1. Provide a standardized payment processing interface.
2. Validate payment requests before processing.
3. Centralize core payment processing logic.
4. Isolate PSP-specific integration complexity.
5. Support multiple payment providers.
6. Make adding new PSPs easier.
7. Provide consistent payment responses.
8. Maintain reliable payment status and lifecycle management.
9. Provide scalable and maintainable microservices.
10. Support comprehensive automated testing.
11. Follow secure payment-processing practices.
12. Minimize changes required in the Merchant System when adding new payment providers.

## Related Repository

Common testing and automation repository:

```text
payment-platform-test-suite
```

This repository contains shared test automation and API testing assets for the Payment Platform.

## License

Add the applicable license information here.
