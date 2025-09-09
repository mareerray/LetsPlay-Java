# Product repository
> Here’s a detailed explanation of the Product repository in Spring Boot with MongoDB:

### Why Do We Need a Repository?
- A repository acts as the “middleman” between your database (MongoDB) and your Java code.

- It provides a standard, reusable interface for common database operations: Create, Read, Update, Delete (CRUD).

- Using Spring Data, it saves you from writing boilerplate code for every query—Spring Boot generates implementation automatically based on your entity and interface definitions.

### What Does This Do Exactly?
- When you create an interface such as ProductRepository extends MongoRepository<Product, String>, Spring Boot automatically creates a class behind the scenes that connects your code to the MongoDB database.

- This repository lets you do things like:

    - findAll() - fetch all products

    - findById(id) - fetch one product by its id

    - save(product) - add or update a product in the database

    - deleteById(id) - remove a product by its id

- You can also easily add custom methods (for example, findByUserId(userId)) just by defining their method signatures in the interface—no manual query code required.

### Quick Example (How You Use It) in Service/Controller class
````
@Autowired
private ProductRepository productRepository;

// Get all products
List<Product> products = productRepository.findAll();

// Save a product
productRepository.save(new Product(...));

// Find products for a user
List<Product> userProducts = productRepository.findByUserId("user123");

// Delete product
productRepository.deleteById("product456");
````
Spring injects the implementation so all you need is the interface definition.

### Summary Table

| Reason to Use      | 	What It Does for You                       |
|--------------------|---------------------------------------------|
| Reuse code         | 	Common CRUD with no boilerplate            |
| Easy queries       | 	Find by field names, auto-implemented      |
| MongoDB connection | 	Links your model classes to the database   |
| Integration        | 	Works smoothly with Spring Boot dependency |
#### In short:
Repositories are essential for using MongoDB in a modern, secure, and efficient way within Spring Boot applications. They make CRUD simple and your code far easier to maintain and test.

