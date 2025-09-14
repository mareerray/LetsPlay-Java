>Here’s a new, detailed user and product workflow example you can run through your TEST.md, step by step, to thoroughly test your Spring Boot CRUD API with Postman. This combines authentication, all CRUD operations, and error scenario validation for realistic, systematic API testing.
>
> Example Test Scenario: End-to-End User + Product CRUD Flow

## Step 1: Register a New User
Method: POST

URL: http://localhost:8080/users/register

Body (raw, JSON):
````
json
{
"name": "TestUser",
"email": "testuser@example.com",
"password": "TestUser123!"
}
````
Expectation:
````
Status 200

Body: "Registration successful!"
````
## Step 2: Login as TestUser and Save JWT
Method: POST

URL: http://localhost:8080/users/login

Body (raw, JSON):
````
json
{
"email": "testuser@example.com",
"password": "TestUser123!"
}
````
Expectation:
````
Status 200

Body: Contains "token": "<JWT_TOKEN>", "message": "Login successful!"
````
>Action:
>
>Save the JWT token for next steps.

## Step 3: Find Your userId
- Option 1: Query MongoDB directly (if no user info API).

    - Use Mongosh:

````
db.users.find({email: "testuser@example.com"})
````
>Save _id as userId.


- Option 2: If available, call /users/me or similar endpoint with your JWT to get user info (optional).

## Step 4: Create a Product For TestUser
Method: POST

URL: http://localhost:8080/products

Headers:

````
Authorization: Bearer <your_token>
````
Body (raw, JSON):
````
json
{
"name": "Coffee Mug",
"description": "A stylish mug",
"price": 5.99,
"userId": "<your userId>"
}
````
Expectation:
````
Status 200 or 201

Body: Contains "name": "Coffee Mug" and "userId" matches above.
````
## Step 5: Read All Products (Public)
Method: GET

URL: http://localhost:8080/products

Expectation: 
````
Status 200, array includes newly created product.
````
## Step 6: Read Single Product By ID
Method: GET

URL: http://localhost:8080/products/<product_id>

Expectation: 
````
Status 200, shows correct product data.
````
## Step 7: Update Product
Method: PUT

URL: http://localhost:8080/products/<product_id>

Headers: (set JWT)

Body:
````
json
{
"name": "Coffee Mug XL",
"description": "A larger stylish mug",
"price": 7.99,
"userId": "<your userId>"
}
````
Expectation: 
````
Status 200, product updated.
````
## Step 8: Get Products By User
Method: GET

URL: http://localhost:8080/products/user/<your_userId>

Expectation: 
````
Returns products created by TestUser.
````
## Step 9: Delete Product
Method: DELETE

URL: http://localhost:8080/products/<product_id>

Headers: (set JWT)

Expectation: 
````
Status 200 or 204, resource deleted.
````
## Step 10: Error Checks
- Try to create a product with invalid (missing required) fields – expect 400.

- Try to login with wrong password – expect 401.

- Try to access protected product endpoints without JWT – expect 401.

- Try fetching or deleting a non-existing product ID – expect 404.

Notes for TEST.md
Document expected input/output, status codes, and any variables (like token and id values).

For each request, record the result (pass/fail, actual vs. expected).

Optionally, for each step, add suggestions for automating the test in Postman’s Tests tab.

This scenario will validate your API’s basic functionality, data linkage between user/product, security logic, and error handling, using realistic API requests.