| Method / endpoint     | Purpose                   | Authorized user               | method name     |
|-----------------------|---------------------------|-------------------------------|-----------------|
| .................     | .................         | ...............               | .............   |
| Users CRUD            | .................         | ...............               | .............   |
| .................     | .................         | ...............               | .............   |
| POST /register        | Register                  | permit ALL (public)           | registerUser    |
| POST /login           | Log-in                    | permit ALL (public)           | login           |
| .................     | .................         | ...............               | .............   |
| GET /users            | Get all users             | ADMIN                         | getAllUser      |
| GET /users/{id}       | Get a single user         | ADMIN                         | getUser         |
| POST /users           | Create a new user         | ADMIN                         | createUser      |
| PUT /users/{id}       | Update a user info        | ADMIN                         | updateUser      |
| DELETE /users/{id}    | Delete a user             | ADMIN                         | deleteUser      |
| .................     | .................         | ...............               | .............   |
| GET /users/me         | Get user's own info       | profile owner (ADMIN or user) | getMyProfile    |
| PUT /users/me         | Update user's own profile | profile owner (ADMIN or user) | updateMyProfile |
| DELETE /users/me      | Delete user's own profile | profile owner (ADMIN or user) | deleteMyProfile |
| .................     | .................         | ...............               | .............   |
| Products CRUD         | .................         | ...............               | .............   |
| .................     | .................         | ...............               | .............   |
| GET /products         | Get all products          | permit ALL (public)           | getAllProducts  |
| GET /products/{id}    | Get a single product      | permit ALL (public)           | getProductById  |
| .................     | .................         | ...............               | .............   |
| GET /products/me      | Get own products          | product owner (ADMIN or user) | getMyProducts   |
| .................     | .................         | ...............               | .............   |
| POST /products        | Create a product          | ADMIN or user                 | createProduct   |
| PUT /products/{id}    | Update a product          | ADMIN or product owner        | updateProduct   |
| DELETE /products/{id} | Delete a product          | ADMIN or product owner        | deleteProduct   |
| .................     | .................         | ...............               | .............   |

