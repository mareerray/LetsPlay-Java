package com.gritlab.letsplay.exception;

import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.Map;
import java.util.HashMap;

/**
 * Handles all exceptions thrown by controllers, for robust error handling and audit trace.
 * Order groups: DB integrity, input validation, routing, resource, security, domain, fallback.
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    // === 1. Database integrity and uniqueness ===

    /**
     * Handles DB-level duplicate key errors from MongoDB (e.g. compound unique index violation).
     * Ensures data integrity for critical fields like name, description, price.
     * Status: 409 Conflict
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Object> handleMongoDuplicateKey(DuplicateKeyException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "status", "error",
                        "message", "Product with the same name, description, and price already exists."
                ));
    }

    // === 2. Input validation and parsing ===

    /**
     * Handles Spring validation errors (@Valid on DTOs, missing/wrong fields).
     * Provides detailed feedback for client error correction.
     * Status: 400 Bad Request
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(Map.of(
                "status", "error",
                "message", "Validation failed",
                "fields", errors
        ));
    }

    /**
     * Handles malformed or missing JSON body errors.
     * Status: 400 Bad Request
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleNotReadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "status", "error",
                "message", "Malformed or missing request body"
        ));
    }


    // === 3. Routing and HTTP Method errors ===

    /**
     * Handles wrong/missing HTTP methods for endpoints.
     * Promotes RESTful API best practice.
     * Status: 405 Method Not Allowed
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(Map.of(
                        "status", "error",
                        "message", "HTTP method not allowed for this endpoint."
                ));
    }

    /**
     * Handles completely unmapped controller routes (URL typos, missing endpoints).
     * Requires 'spring.mvc.throw-exception-if-no-handler-found=true'.
     * Status: 404 Not Found
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Object> handleNoHandlerFound(NoHandlerFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "status", "error",
                        "message", "The requested route does not exist."
                ));
    }

    // === 4. Resource-level errors ===

    /**
     * Custom: Handles valid endpoints but missing/unknown resource data.
     * E.g. non-existent product/user/etc.
     * Status: 404 Not Found
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "status", "error",
                        "message", ex.getMessage()
                ));
    }


    // === 5. Security and authentication/authorization ===

    /**
     * Handles authentication failures due to bad credentials (Spring Security).
     * Status: 401 Unauthorized
     */
    @ExceptionHandler(org.springframework.security.authentication.BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentials(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("status", "error", "message", "Invalid authentication credentials."));
    }

    /**
     * Handles missing authentication credentials (Spring Security).
     * Status: 401 Unauthorized
     */
    @ExceptionHandler(org.springframework.security.authentication.AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<Object> handleAuthCredentialsNotFound(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("status", "error", "message", "Authentication required. Please log in."));
    }

    /**
     * Handles forbidden resource access (Spring Security).
     * Status: 403 Forbidden
     */
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDenied(Exception ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("status", "error", "message", "You do not have permission to perform this action."));
    }

    /**
     * Custom: Handles domain-specific unauthorized access.
     * Status: 401 Unauthorized
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object>  handleUnauthorized(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("status", "error", "message", ex.getMessage()));
    }


    /**
     * Custom: Handles domain-specific forbidden access.
     * Status: 403 Forbidden
     */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Object>  handleForbidden(ForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("status", "error", "message", ex.getMessage()));
    }

    // === 6. Application/domain-specific logic errors ===

    /**
     * Custom: Handles already registered user (duplicates).
     * Status: 409 Conflict
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserExists(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("status", "error", "message", "User with the given email already exists."));
    }

    /**
     * Handles bad arguments in requests, usually app domain errors.
     * Status: 400 Bad Request
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest()
                .body(Map.of("status", "error", "message", ex.getMessage()));
    }

    /**
     * Custom: Handles invalid user roles specification.
     * Status: 400 Bad Request
     */
    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<Object> handleInvalidRole(InvalidRoleException ex) {
        return ResponseEntity.badRequest()
                .body(Map.of("status", "error", "message", ex.getMessage()));
    }

    // === 7. Fallback - any other unhandled/internal error ===

    /**
     * Handles any other uncaught exceptions (framework, runtime, programming errors).
     * Prevents leaking stack traces to API client, useful for auditing technical issues.
     * Status: 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleOther(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("status", "error", "message",
                        "We're experiencing technical difficulties. Please try again later."));
    }

    // === Custom application exceptions (used for type safety) ===

    /** Not found: used when requested model/data is missing. */
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    /** Used when an attempted registration is for an already-existing user. */
    public static class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException() {
            super("User already exists");
        }
    }

    /** Used when a supplied role is not valid for the app/domain. */
    public static class InvalidRoleException extends RuntimeException {
        public InvalidRoleException(String message) {
            super(message);
        }
    }

    /** App-specific unauthorized error. */
    public static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String message) { super(message); }
    }

    /** App-specific forbidden error. */
    public static class ForbiddenException extends RuntimeException {
        public ForbiddenException(String message) { super(message); }
    }
}

