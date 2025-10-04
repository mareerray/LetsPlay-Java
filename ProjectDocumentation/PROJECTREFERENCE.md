# Project Reference: Spring Boot + MongoDB Architecture
## Table of Contents
- [Entities](#1-entities)
- [DTOs](#2-dtos)
- [Repository Interfaces](#3-repository-interfaces)
- [REST Controllers](#4-rest-controllers)
- [Security, Hashing, JWT, Beans, and Salting](#5-security-hashing-jwt-beans-and-salting)
- [Global Exception Handling](#6-global-exception-handling)
- [Application Properties & Configuration](#7-application-properties--configuration)
- [Project Flow & Example API Call](#8-project-flow--example-api-call)
- [Summary Table: CRUD, Auth, Validation, and Error Mapping](#summary-table-crud-auth-validation-and-error-mapping)

## 1. Entities
  > User: Maps to a MongoDB users collection.
  > 
  > Fields: id, name, email, password (hashed and salted), role.

  > Product: Maps to a MongoDB products collection.
  > 
  > Fields: id, name, description, price, userId (user ownership).

---
## 2. DTOs
  > UserDTO: For secure user info output (never exposes password).

  > UserRegistrationDTO, UserLoginDTO: Input objects that enforce validation rules for registration/login requests.

  > ProductDTO, ProductInputDTO, ProductUpdateDTO: Separate data structures for reading/updating product data, ensuring security and validation.

  > LoginResponseDTO: Contains status messages and the JWT token returned at login.

[Back to Table of Contents](#table-of-contents)

---
## 3. Repository Interfaces
  > UserRepository, ProductRepository: Extend MongoRepository<Entity, ID>, allowing CRUD plus custom queries.

- Behind the scenes: Spring Boot creates implementation classes for you, so you write only interfaces and method signatures.

- Why? No need to write raw queries for common use cases—Spring Data provides it with best practices and security by default.

[Back to Table of Contents](#table-of-contents)

---
## 4. REST Controllers
  > UserController: Handles user registration, login, self-info, etc.

  > ProductController: Full RESTful CRUD for products, using repository interfaces and DTOs for security, validation, and clear API structure.

[Back to Table of Contents](#table-of-contents)

---
## 5. Security, Hashing, JWT, Beans, and Salting
- **Hash code**: When you set or check a password, the plain text is passed through a secure hash function (like BCrypt). Java also uses hash codes internally for quick lookups, but in security, a hash makes sure the password is stored securely and is unrecoverable in plain text.

- **Salt**: Every password is hashed with a random salt—extra data added before hashing. This guarantees that even if two people set the same password, their password hashes are unique and much harder to crack for attackers.

- **Bean**: In Spring, a "bean" is a managed component (marked with @Component, @Service, @Bean, etc.). Beans form the wiring of your application—Spring instantiates and injects them wherever needed, promoting modularity and testability.

- **JWT Token**: On successful login, a JSON Web Token is issued and sent to clients. This token is included in headers for subsequent API requests. The server verifies JWT tokens for authentication/authorization without tracking sessions—scalable, stateless, and secure.

  #### Why these practices?

  - Hash+Salt: Password security.
  - Beans: Decoupled, maintainable architecture. 
  - JWT: Secure and scalable authentication for modern APIs.

[Back to Table of Contents](#table-of-contents)

---
## 6. Global Exception Handling
- **GlobalExceptionHandler**: One place to map all exceptions (validation errors, auth errors, not found, conflicts, etc.) to proper HTTP status and human-readable JSON.

- Benefits: Consistency, traceability, and easy debugging.

[Back to Table of Contents](#table-of-contents)

---
## 7. Application Properties & Configuration
- **Secrets and connection details** (Mongo URI, JWT secret, etc.) are stored in properties files or environment variables, not code.

- **SecurityConfig & Bean setup**: Security chain, JWT filter, and password encoder (using BCrypt with salt) are configured via bean methods.

[Back to Table of Contents](#table-of-contents)

---
## 8. Project Flow & Example API Call
1. Request: Client/user/API consumer calls a REST endpoint.

2. Controller: Handles request, validates input (using DTO+annotations), and enforces authentication with JWT.

3. Repository: Controller delegates data ops to a Spring Data repository.

4. MongoDB: Repository fetches/saves documents transparently.

5. DTO: Sends only safe user/product info to the client.

6. GlobalExceptionHandler: Catches and maps errors.

7. Security: Auth, passwords, and configuration handled with industry best practices.

Example:

- Login: User submits email/password → Password is hashed and salted, checked against MongoDB.

- On success: Server generates a JWT, returns it to the client, who uses it for all subsequent requests.

[Back to Table of Contents](#table-of-contents)

---
## Summary Table: CRUD, Auth, Validation, and Error Mapping
   | Layer/Concept	     | What It Does / Why It Matters                                                                                |
   |--------------------|--------------------------------------------------------------------------------------------------------------|
   | Entity	            | Data model (maps to DB); foundation for everything                                                           |
   | Repository	        | Easy, secure database integration (CRUD/code-free)                                                           | 
   | DTO	               | Security for outbound/inbound data; ensures clients see only intended information                            |
   | Controller	        | Exposes REST API endpoints, coordinates validation, security, data access                                    |
   | Bean	              | Managed component; enables dependency injection and modular code                                             |
   | Hash+Salt	         | Keeps all passwords secure and unique, blocks easy cracking                                                  |
   | JWT Token	         | Stateless, scalable user authentication; token lets users prove their identity without resending credentials |
   | Exception Handler	 | Clean, centralized error mapping (improves user/dev experience)                                              |
   | App Properties	    | Stores secrets/configs away from code for better security and maintainability                                |
   
In short:
This project builds a production-style Spring Boot API with secure authentication, safe password storage, global error handling, 
DTO-based data flow, and a robust, modular architecture—all wired together by Spring’s powerful bean/container system!

[Back to Table of Contents](#table-of-contents)

---