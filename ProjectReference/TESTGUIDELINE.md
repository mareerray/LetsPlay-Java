>To verify all your Spring Boot CRUD API endpoints and authentication flows via Postman, follow this step-by-step guide. This will help ensure that every controller function is working as expected using real HTTP requests.

## 1. Setup Postman and Your API
Make sure your Spring Boot app is running (usually on http://localhost:8080).

Open Postman and create a new workspace for your project to organize requests.

## 2. Test Public Endpoints (No Authentication Required)
> ### a. Get All Products
> 
> Method: GET
> 
> URL: http://localhost:8080/products
>
> Click "Send" and confirm you get a JSON array of products with HTTP 200.

> ### b. Get Single Product
> 
> Method: GET
>
> URL: http://localhost:8080/products/{id} (replace {id} with a real product ID)
>
> Click "Send" and check for HTTP 200 and product details.

## 3. Test Authentication: Register and Login
> ### a. Register User
> 
> Method: POST
>
> URL: http://localhost:8080/users/register
>
> Body (raw, JSON):
>
> json
> ````
> {
> 
> "name": "Mama",
> 
> "email": "mama@mama.com",
> 
> "password": "Mama12345!"
> 
> }
> ````
> Click "Send". 
> 
> Check for success message like "Registration successful!".

> ### b. Login User (to obtain JWT token)
> 
> Method: POST
>
> URL: http://localhost:8080/users/login
>
> Body (raw, JSON):
>
>json
>````
>{
> 
>"email": "mama@mama.com",
>
>"password": "Mama12345!"
> 
>}
> ````
> Click "Send". 
> 
> Save the JWT token from the JSON response (token field).
> 
> Example output body:
> ````
> {
> 
>    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYW1hQG1hbWEuY29tIiwicm9sZSI6InVzZXIiLCJ1c2VySWQiOiI2OGM1YjBhZGY0MTExYTVhOTA3NjllOTAiLCJpYXQiOjE3NTc3ODgxNTQsImV4cCI6MTc1Nzg3NDU1NH0.wXPuVmDU2VavjCtqao8_OvCSwRZJei4dtPDvWj3EJyE",
>
>    "message": "Login successful!"
> 
> }
>````
## 4. Test Protected Endpoints (Using JWT Token)
For actions like Create, Update, and Delete on products, you'll need to include the JWT token.

> ### a. Set Authorization Header
> In Postman, for requests requiring authentication, 
> - go to the "Authorization" tab, 
> - select "Bearer Token", and paste your JWT token. 
> - Alternatively, add a header:
>
> ````
> Key: Authorization
> Value: Bearer your-token
> ex. Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYW1hQG1hbWEuY29tIiwicm9sZSI6InVzZXIiLCJ1c2VySWQiOiI2OGM1YjBhZGY0MTExYTVhOTA3NjllOTAiLCJpYXQiOjE3NTc3ODgxNTQsImV4cCI6MTc1Nzg3NDU1NH0.wXPuVmDU2VavjCtqao8_OvCSwRZJei4dtPDvWj3EJyE
>````
>
> ### Ensure any protected endpoint (e.g., POST, PUT, DELETE) includes this.

> ### b. Create Product
> Method: POST
>
> URL: http://localhost:8080/products
>
> Body (raw, JSON):
>
> json
> ````
>{
>"name": "Widget",
> 
>"description": "Modern widget",
> 
>"price": 9.99,
> 
>"userId": "yourUserId"
> 
>}
> ````
>Click "Send". 
> 
> Check for HTTP 200 and created product JSON.
> 
> Example output body:
> ````
>{
> 
>"id": "68c5bbf2f53a95e442a481d3",
>
>"name": "Widget",
> 
> "description": "Modern widget",
> 
>"price": 9.99,
> 
>"userId": "68c5b0adf4111a5a90769e90"   
> 
>}
>````

> Note: Query your MongoDB directly for the user's email and note the associated id.
> ````
> db.users.find({email:"mama@mama.com"})
> 
> [
>   {
>     _id: ObjectId('68c5b0adf4111a5a90769e90'),  // Use this id as userId
>     name: 'Mama',
>     email: 'mama@mama.com',
>     password: '$2a$10$XsttpwQEJcwp4ysQTBqqgetZQaaRWgLC1nAgdoV59LVschJb7zCDm',
>     role: 'user',
>     _class: 'com.gritlab.letsplay.User'
>   }
> ]
> ````
> This value comes from the _id field in your MongoDB query result, which corresponds to the id field in your Java User entity.

> ### c. Update Product
> Method: PUT
>
> URL: http://localhost:8080/products/{id}
>
> Body: same as create, changing fields as needed
>
> Click "Send". 
> 
> Check for HTTP 200 and updated product.

> ### d. Delete Product
> Method: DELETE
>
> URL: http://localhost:8080/products/{id}
>
> Click "Send". 
> 
> Check for HTTP 200 or 204 No Content.

> ### e. Get Products by User
> Method: GET
>
> URL: http://localhost:8080/products/user/{userId}
>
> Click "Send". 
> 
> Verify correct product list returned.

## 5. Validate Error Handling & Security
   Try invalid requests (missing fields, unauthorized access) and verify correct HTTP error codes and messages.

## 6. Automate and Save
   Save each request in collections for future testing.

### Optionally, write Postman test scripts to validate response data and status codes automatically.

This workflow lets you systematically check each function (CRUD and authentication) of your API with Postman, before moving on to error handling improvements.