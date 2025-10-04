## Database Design
````  
classDiagram
   User "1" -- "n" Product : Owns 
   User : +String id
   User : +String name
   User : +String email
   User : +String password
   User : +String role
   Product : +String id
   Product : +String name
   Product : +String description
   Product : +Double price
   Product : +String userId
````
> This UML class diagram pattern describes the structure and relationships of the data model for your system using class diagram notation—a common way to plan object-oriented (and database) design.

## Meaning of Each Part

### 1. The Relationship:
   User "1" -- "n" Product : Owns

This means one User can own many Products (a "one-to-many" relationship).

Each Product belongs to exactly one User.

In code/database, this is typically enforced by giving the Product an attribute (e.g. userId) that refers to the owning User’s primary key.

### 2. User Class
   Lists attributes for the User class:

+String id: unique identifier for the user

+String name: user's name

+String email: user's email address

+String password: user's password (should be hashed!)

+String role: user's role (admin, user, etc.)

### 3. Product Class
   Lists attributes for the Product class:

+String id: unique identifier for the product

+String name: product name

+String description: description text

+Double price: product price

+String userId: references the owning user

How This Maps to Code/Database Design
You’ll create two entities: User and Product.

Each Product has a field (userId) pointing to its User owner.

The diagram helps visualize that a User can "own" (have created or be associated with) multiple Products, but each Product belongs to only one User.

### Summary:
This pattern formalizes the structure for user management and product ownership in your application—allowing you to build REST APIs for users and products, linking them together in code and the MongoDB database.
