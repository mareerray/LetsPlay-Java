
# LetsPlay Project
RESTful CRUD API with Spring Boot & MongoDB
### Author : [Mayuree Reunsati](https://github.com/mareerray)

### Table of Contents
- [Objectives](#objectives)
- [Features](#features)
- [Setup & Running](#setup--running)
- [Requirements](#requirements)
- [Quickstart](#quickstart)
- [API Endpoints](#api-endpoints)
- [Authentication](#authentication)
- [User Management](#user-management)
- [Product Management](#product-management)
- [Authentication & Authorization](#authentication--authorization)
- [Security Measures](#security-measures)
- [Error Handling](#error-handling)
- [Testing & Auditing](#testing--auditing)
- [Resources](#resources)

---
## Objectives
Develop a secure, robust, and RESTful CRUD API using Spring Boot and MongoDB.
The app supports user management and product management with full authentication, authorization, error handling, and security controls.

[Back to Table of Contents](#table-of-contents)

---
## Features
- User Management: Register, login, view/update/delete users, admin role support.

- Product Management: Create, read, update, delete products.

- GET Products API is open (no authentication required).

- Role-Based Access Control: Only authorized users/admins can access protected resources.

- Token-based Authentication: Powered by JWT (JSON Web Tokens).

- Secure Password Storage: Hash/salt passwords before saving.

- RESTful Principles: All endpoints and behaviors are consistent with REST conventions.

- Comprehensive Error Handling: All errors are gracefully mapped to proper HTTP codes.

[Back to Table of Contents](#table-of-contents)

---
## Setup & Running
### Requirements
- Java 17+

- Maven

- MongoDB (local or cloud/Atlas)

- HTTPS-capable browser or API client (Postman, curl)

### Quickstart
### 1. Clone Repository

````
git clone <your-repo-url>
cd letsplay-java
````

### 2. Configure MongoDB URI in application.properties
````
spring.application.name=letsplay
spring.data.mongodb.uri=mongodb://localhost:27017/test
spring.security.user.name=maire
spring.security.user.password=maire123
jwt.secret=your-very-secret-key

server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=password
server.ssl.keyStoreType=PKCS12
server.ssl.keyAlias=tomcat

spring.mvc.throw-exception-if-no-handler-found=true
spring.web.resources.add-mappings=false
````
### 3. Place your SSL keystore

- Be sure keystore.p12 is present in src/main/resources

- If you need one, generate with:

````
keytool -genkeypair -alias tomcat -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore keystore.p12 -validity 3650
````

### 4. Build and Run

````
mvn spring-boot:run
````

- Access your API at: https://localhost:8080/

(Note: Browsers and Postman may warn about untrusted or self-signed certificate; you can safely accept this for testing.)

[Back to Table of Contents](#table-of-contents)

---
## API Endpoints
### Authentication (No auth required)
> POST /users/register

- Register a new user

- Required fields: name, email, password

> POST /users/login

- Authenticate and get JWT token

- Required fields: email, password

### User Management
| Method	 | Endpoint	     | Auth Required	 | Description                   |
|---------|---------------|----------------|-------------------------------|
| GET     | 	/users/me    | 	Yes	          | Get own user profile          |
| PUT	    | /users/me     | 	Yes           | 	Update own user info         |
| DELETE  | 	/users/me    | 	Yes           | 	Delete own account           |
| GET     | 	/users/      | 	Admin         | 	Get all users (admin only)   |
| GET     | 	/users/{id}	 | Admin          | 	Get any user (admin only)    |
| POST    | 	/users       | Admin          | 	Create new user (admin only) |
| DELETE  | 	/users/{id}  | 	Admin         | 	Delete any user (admin only) |

### Product Management
| Method	 | Endpoint	       | Auth Required	 | Description                  |
|---------|-----------------|----------------|------------------------------|
| GET	    | /products	      | No	            | List all products            |
| GET	    | /products/{id}  | No	            | Get single product           |
| POST	   | /products	      | Yes	           | Create product               |
| PUT	    | /products/{id}	 | Yes	           | Update product (owner/admin) |
| DELETE  | /products/{id}	 | Yes	           | Delete product (owner/admin) |

[Back to Table of Contents](#table-of-contents)

---
## Authentication & Authorization
- Users authenticate using JWT tokens (supplied as Authorization: Bearer <token>)

- Role-based access:

    - user role: Can access/modify own resources

    - admin role: Full access to all resources

[Back to Table of Contents](#table-of-contents)

---
## Security Measures

- All traffic uses HTTPS for security.

- Passwords are hashed and salted using BCryptPasswordEncoder.

- Input validation: Strict validation with @Valid, custom DTOs.

- No sensitive data (passwords, secrets) returned in any API response.

[Back to Table of Contents](#table-of-contents)

---
## Error Handling
All errors are handled globally and consistently, with informative JSON responses and appropriate status codes.

| Error/Condition       | 	HTTP Status	 | Example                                            |
|-----------------------|---------------|----------------------------------------------------|
| Not Found             | 	404	         | User/Product does not exist                        |
| Unauthorized          | 	401          | 	JWT missing/invalid, login required               |
| Forbidden             | 	403          | 	Accessing/changing another user’s product or info |
| Conflict              | 	409          | 	Registering duplicate user or product             |
| Validation Error      | 	400          | 	Missing/wrong fields                              |
| Internal Server Error | 	500          | 	Unexpected bug/system error                       |

[Back to Table of Contents](#table-of-contents)

---
## Testing & Auditing
- Automated tests for all endpoints and error scenarios recommended.

- Auditors can run the project using Maven and test with Postman/curl as shown in the setup.

- CORS, rate limiting, and additional features are available for further enhancement 
and can be added for production deployments or advanced security policies.

[Back to Table of Contents](#table-of-contents)

---
## Resources
- Spring Initializer

- MongoDB Documentation

- Spring REST Docs

For questions or improvements, open an issue or contact the author.

[Back to Table of Contents](#table-of-contents)

---
