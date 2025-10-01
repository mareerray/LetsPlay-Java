>To verify all your Spring Boot CRUD API endpoints and authentication flows via Postman, follow this step-by-step guide. This will help ensure that every controller function is working as expected using real HTTP requests.

# Setup Postman and Your API
Make sure your Spring Boot app is running (usually on https://localhost:8080).

Open Postman and create a new workspace for your project to organize requests.
>Here's your Postman testing checklist with sample request bodies and expected outputs for each key scenario. Adjust URLs as needed!

# USER ENDPOINTS
> Register User
> 
>POST /users/register
> 
> URL: http://localhost:8080/users/register

````
  {
    "name": "Alice",
    "email": "alice@example.com",
    "password": "VeryStrongPassword123"
  }
````
Expected Success Output
````
"Registration successful!"
````
Expected Error (duplicate email or invalid email):

````
  {
    "status": "error",
    "message": "User with the given email already exists."
  }

or

  {
    "status": "error",
    "message": "Validation failed",
    "fields": {
      "email": "must be a valid email address"
    }
  }
````
>Login
>
>POST /users/login
> 
> URL: http://localhost:8080/users/login

````
  {
    "email": "alice@example.com",
    "password": "VeryStrongPassword123"
  }
````
Expected Success (JWT shown):

````
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "message": "Login successful!"
  }
````
Expected Error:

````
  {
    "status": "error",
    "message": "Invalid authentication credentials."
  }
````
>Get Own Profile
>
>GET /users/me
> 
> URL: http://localhost:8080/users/me
> 
> — Header: Authorization: Bearer <JWT>

Expected Success:

````
  {
    "id": "...",
    "name": "Alice",
    "email": "alice@example.com",
    "role": "user"
  }
````
Expected Error (no token):

````
  {
    "status": "error",
    "message": "Authentication required. Please log in."
  }
````
>Update Own Profile
>
>PUT /users/me
> 
> URL: http://localhost:8080/users/me
>
> — Header: Authorization: Bearer <JWT>

````
  {
    "name": "Alice Updated",
    "email": "alice@example.com",
    "password": "NewStrongPassword123"
  }
````
Expected Success: 
````
Updated user profile as above.
````

Expected Error (missing field or invalid):

````
  {
    "status": "error",
    "message": "Validation failed",
    "fields": {
      "email": "must be a valid email address"
    }
  }
````
>Delete Own Profile
>
>DELETE /users/me
> 
> URL: http://localhost:8080/users/me
>
> — Header: Authorization: Bearer <JWT>

Expected Success: 
````
"User deleted"
````

# ADMIN USER ENDPOINTS (Use Admin JWT)
>Get All Users
>
>GET /users
> 
> URL: http://localhost:8080/users
>
> — Header: Authorization: Bearer <JWT>

Expected Output:
````
List of UserDTOs
````
Expected Error (non-admin):

````
  {
    "status": "error",
    "message": "You do not have permission to perform this action."
  }
````
>Create User (admin)
>
>POST /users
> 
> URL: http://localhost:8080/users
>
> — Header: Authorization: Bearer <JWT>

````
  {
    "name": "Bob",
    "email": "bob@example.com",
    "password": "StrongAdminPass123",
    "role": "admin"
  }
````
Expected Success: 
````
User DTO
````
Expected Error (invalid role):

````
  {
    "status": "error",
    "message": "Role must be either 'admin' or 'user'."
  }
````
# PRODUCT ENDPOINTS

>Get All Products (public)
>
>GET /products
> 
> URL: http://localhost:8080/products
>
> — Header: Authorization: Bearer <JWT>
Expected Output: 
````
List of ProductDTOs
````
>Get Single Product (public)
>
>GET /products/{id}
> 
> URL: http://localhost:8080/products/{id}

Expected Output:

````
  {
    "id": "...",
    "name": "Chess Set",
    "description": "Classic chess set...",
    "price": 24.99,
    "userId": "..."
  }
````
Expected Error (bad id):

````
  {
    "status": "error",
    "message": "Product not found with id: ..."
  }
````
>Create Product
>
>POST /products
> 
> URL: http://localhost:8080/products
> 
> — Header: JWT

````
  {
    "name": "Chess Set",
    "description": "Classic chess set with pieces.",
    "price": 24.99
  }
````
Expected Success: 
````
Product DTO
````
Expected Error (no token):

````
  {
    "status": "error",
    "message": "User not authenticated."
  }
````
Expected Error (bad input):

````
  {
    "status": "error",
    "message": "Validation failed",
    "fields": {
      "price": "must be greater than 0"
    }
  }
````
>Update Product
>
>PUT /products/{id} 
> 
> URL: http://localhost:8080/products/{id}
> 
> — Header: JWT

````
  {
    "name": "Travel Chess Set",
    "description": "Foldable, for travel.",
    "price": 19.99
  }
````
Expected Success: 
````
Updated Product DTO
````
Expected Error (not owner or admin):

````
  {
    "status": "error",
    "message": "You don't have permission to modify this product."
  }
````
>Delete Product
>
>DELETE /products/{id} 
> 
> URL: http://localhost:8080/products/{id}
> 
> — Header: JWT

Expected Success: 
````
"Product deleted"
````
Expected Error (not owner or admin):

````
  {
    "status": "error",
    "message": "You don't have permission to delete this product."
  }
````
>Get My Products
>
>GET /products/me 
> 
> URL: http://localhost:8080/products/me
> 
> — Header: JWT

Expected Output:
````
List of ProductDTOs (owned by user)
````
Expected Error (no token):

````
  {
    "status": "error",
    "message": "User not authenticated."
  }
````
>This checklist and payloads will allow you to cover all functional and error scenarios in Postman and for your audit.product, security logic, and error handling, using realistic API requests.