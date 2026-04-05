# HHGCL Project

This project consists of two modules: `core-security-starter` and `sample-application`.

## Modules

- **core-security-starter**: A Spring Boot starter library providing security features including JWT authentication, user management, and common DTOs.
- **sample-application**: A sample Spring Boot application demonstrating the usage of the `core-security-starter`.

## Prerequisites

- Java 21
- Maven 3.6+

## Building the Project

To build both modules, run the following command from the root directory:

```bash
mvn clean install
```

This will compile, test, and package both modules.

## Running the Sample Application

After building, navigate to the `sample-application` directory and run:

```bash
mvn spring-boot:run
```

The application will start on the default port (8080) unless configured otherwise.

### Environment Variables

The application requires the following environment variables (or a `.env` file):

- `APPLICATION_NAME`: Name of the application
- `ACTIVE_PROFILE`: Active Spring profile (e.g., `dev`, `prod`)
- `JWT_SECRET`: Secret key for JWT token generation
- `JWT_EXPIRATION`: JWT token expiration time in milliseconds

Example `.env` file:

```
APPLICATION_NAME=SampleApp
ACTIVE_PROFILE=dev
JWT_SECRET=your-secret-key-here
JWT_EXPIRATION=3600000
```

## API Endpoints

### Authentication

- `POST /auth/register`: Register a new user
- `POST /auth/login`: Login and receive a JWT token

### Public

- `GET /api/public/health`: Health check endpoint

### User (Requires Authentication)

- `GET /api/user/me`: Get current user information

### Admin (Requires ADMIN role)

- `GET /api/admin/users`: Get all users with pagination

## Example Requests

### Register a New User
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "firstName": "Test",
    "lastName": "User",
    "email": "test@example.com",
    "role": "USER",
    "password": "Password1"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "Password1"
  }'
```

Response includes JWT token:
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "username": "testuser",
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  }
}
```

### Access Protected Endpoint
```bash
curl -X GET http://localhost:8080/api/user/me \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

### Health Check
```bash
curl -X GET http://localhost:8080/api/public/health
```

## Testing

The project includes comprehensive test coverage with both integration and unit tests.

### Integration Tests
Integration tests are included in the `sample-application` module, covering controller endpoints:
- Authentication endpoints (register, login)
- Public health check
- User profile retrieval
- Admin user management with pagination

### Unit Tests
Unit tests provide detailed coverage of business logic:
- **AuthServiceImplTest**: User registration and login logic
- **TestServiceImplTest**: Health checks and user data retrieval
- **JwtServiceTest**: JWT token generation, validation, and claims extraction
- **CustomUserDetailsTest**: Spring Security user details implementation
- **MappersTest**: Data transfer object mapping

Run all tests with:

```bash
mvn test
```

The test suite achieves over 90% code coverage, ensuring reliability and maintainability of the application.

## Design Decisions and Trade-offs

### Architecture
- **Clean Architecture**: The project follows clean architecture principles with clear separation of concerns. Entities, repositories, services, and controllers are layered appropriately, promoting maintainability and testability.
- **Multi-Module Design**: Separated into `core-security-starter` (reusable library) and `sample-application` (demo app) to demonstrate modularity and reusability.

### Security
- **JWT Authentication**: Chosen for stateless authentication, allowing scalability. Trade-off: Requires careful token management and expiration handling.
- **Role-Based Access Control**: Implemented using Spring Security's `@PreAuthorize` for method-level security, providing fine-grained access control.
- **Password Encoding**: Uses BCrypt for secure password hashing.

### Cross-Cutting Concerns
- **Centralized in Core Library**: JWT filtering, exception handling, and configuration properties are implemented in the `core-security-starter` module, ensuring reusability across applications.
- **Global Exception Handling**: Custom exception classes with HTTP status mapping provide consistent error responses.
- **Logging**: Integrated user and endpoint logging in the JWT filter for audit trails.

### Technology Choices
- **Spring Boot**: Provides auto-configuration and simplifies development. Trade-off: Opinionated framework may limit customization.
- **H2 Database**: Used for testing to avoid external dependencies. Production would use a persistent database.
- **Lombok**: Reduces boilerplate code. Trade-off: Requires IDE plugin and may obscure generated code.

### Production Readiness
- **Configuration Properties**: Externalized security settings for different environments.
- **Error Handling**: Structured error responses with appropriate HTTP status codes.
- **Validation**: Input validation using Bean Validation annotations.
- **Trade-off**: Additional complexity for robustness vs. simpler implementation.
