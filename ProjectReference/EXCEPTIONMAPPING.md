
## UserController Error Scenarios & Exception Mapping
| Error Type	        | How It’s Handled        | Example Case|
|--------------------|-------------------------|-------------|
| 404 Not Found	     | ResourceNotFoundException	 | User not found by ID or email (e.g., GET /users/12345, user does not exist). |
| 401 Unauthorized	  | UnauthorizedException or Spring Security exceptions (AuthenticationCredentialsNotFoundException, BadCredentialsException)	 | Invalid credentials at login, JWT missing/stale, trying to access user info without being logged in.|
| 409 Conflict	      | UserAlreadyExistsException	 | Registration or admin creation of a user with an existing email, e.g., POST /users with an email that's already taken. |
| 403 Forbidden	     | ForbiddenException or Spring Security's AccessDeniedException	 | User tries to modify someone else’s data, or assign privileged roles without authority. |
| 400 Validation	    | MethodArgumentNotValidException (Spring's automatic validation)	 | Registration payload is missing fields or format is wrong. E.g., no password, invalid email. |
| Generic (500)	     | Exception (fallback, all other uncaught errors)	 | Programming errors, system failures, bugs not covered by above scenarios. |

## ProductController Error Scenarios & Exception Mapping 
| Error Type	| How It’s Handled	      | Example Case |
| -------------- |------------------------|--------------|
| 404 Not Found	| ResourceNotFoundException	 | Product get/update/delete with invalid ID (e.g., trying to update a product that is deleted/not present). |
401 Unauthorized | 	UnauthorizedException or Spring Security exceptions | 	User tries to create/update a product without logging in or with a bad JWT.|
403 Forbidden | 	ForbiddenException or Spring Security's AccessDeniedException | 	Non-owner/user tries to modify/delete a product owned by another user.|
400 Validation | 	MethodArgumentNotValidException	 | Create/update product endpoint with missing required fields or price format error. |
Generic (500) | 	Exception (fallback) | 	Any unhandled runtime/server/internal error. |

## With GlobalExceptionHandler in Place
Every controller scenario is handled **centrally and consistently**.
>No more repetition or custom error handling logic in every controller—just throw the relevant exception and the GlobalExceptionHandler converts it to:

- Proper HTTP status code

- Consistent, user-friendly JSON error message

- Auditability (all error cases handled and logged in one location)

### Case Example: Product Create
Scenario:

- User tries to create a product with identical name/description/price

- MongoDB throws a DuplicateKeyException due to unique index
Handler:

````
@ExceptionHandler(DuplicateKeyException.class)
public ResponseEntity<Object> handleMongoDuplicateKey(DuplicateKeyException ex) {
return ResponseEntity.status(HttpStatus.CONFLICT)
.body(Map.of("status", "error", "message", "Product with the same name, description, and price already exists."));
}
````
API Response:

````
{
"status": "error",
"message": "Product with the same name, description, and price already exists."
}
````

### Case Example: Unauthorized Access
Scenario:

- User is not logged in, tries to access /users/me
Handler:

````
@ExceptionHandler(org.springframework.security.authentication.AuthenticationCredentialsNotFoundException.class)
public ResponseEntity<Object> handleAuthCredentialsNotFound(Exception ex) {
return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
.body(Map.of("status", "error", "message", "Authentication required. Please log in."));
}
````

### Case Example: Validation Error
Scenario:

- POST /users with missing required field password
Handler:

````
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
// Details omitted for brevity
}
````

## Summary Table (Mapping: Controller → Global Handler → API Response)
| Error/Condition	| Exception Type	| HTTP Status	| Example | 	UserController   | 	ProductController |
| ---------------- | ----------------- | -------------- | -------- | ----------------- | --------------------- |
| 404 Not Found	| ResourceNotFoundException	| 404	| Get user/product not in DB|✓                | 	✓                 |
| 401 Unauthorized	| UnauthorizedException/AuthExceptions	| 401	| No JWT, invalid credentials|✓                | 	✓                 |
| 403 Forbidden	| ForbiddenException/AccessDenied	| 403	| Editing another user's product|✓                | 	✓                 |
| 409 Conflict	| UserAlreadyExistsException/DuplicateKeyException	| 409	| Duplicate email/product|✓                | 	✓                 |
| 400 Validation Error	| MethodArgumentNotValidException	| 400	| Missing/bad fields|✓                | 	✓                 |
| 500 Internal Server Error	| Exception (generic)	| 500	| System/DB/Config error |✓                | 	✓                 |

Adding a GlobalExceptionHandler gives you predictable, auditable, and maintainable error handling across all controllers.
You can now easily trace any error back to its handler, know its cause, and communicate it clearly to clients and developers.

