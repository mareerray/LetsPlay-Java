# Concrete Design Patterns in LetsPlay Spring Boot Project
## Table of Contents

- [Dependency Injection (DI) / Inversion of Control (IoC)](#1-dependency-injection-di--inversion-of-control-ioc)
- [Singleton](#2-singleton)
- [Factory Method (with @Bean)](#3-factory-method-with-bean)
- [Controller Pattern](#4-controller-pattern)
- [Repository Pattern](#5-repository-pattern)
- [DTO (Data Transfer Object) Pattern](#6-dto-data-transfer-object-pattern)
- [Strategy Pattern (Filter, Exception Handler)](#7-strategy-pattern-filter-exception-handler)
- [Summary Table: Concrete Patterns in this Project](#summary-table-concrete-patterns-in-this-project)

### How each design pattern actually works in this project?

## 1. Dependency Injection (DI) / Inversion of Control (IoC)

#### How it works:

- You never manually create or connect this dependencies (like repositories); Spring does this for you.

- You use @Autowired for fields, constructors, or methods, and Spring automatically supplies the right instance when this app runs.

- Spring scans for classes marked with annotations like @Component, @Service, @Repository, or @Controller. It then creates and manages their instances (called beans), and injects them wherever needed.

#### Example:

````
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    // ...other code
}
````
> What this means: You can use userRepository in this controller, 
> even though you never wrote new UserRepository() — Spring does all the wiring!

[Back to Table of Contents](#table-of-contents)

---

## 2. Singleton
#### How it works:

- By default, each Spring bean is a singleton (only one instance exists for the whole app lifecycle).

- Controllers, repositories, filters marked as beans, etc., exist only once in memory.

- Spring manages and reuses them wherever they're injected.

#### Example:

UserController, ProductController, JwtAuthenticationFilter, UserRepository, etc., 
are all singleton beans unless you explicitly tell Spring otherwise.

[Back to Table of Contents](#table-of-contents)

---

## 3. Factory Method (with @Bean)
#### How it works:

- You use methods marked with @Bean inside a @Configuration class to customize building of beans.

- Spring calls these factory methods when starting up and then manages the returned objects as beans.

#### Example (AdminBootstrap):

````
@Configuration
public class AdminBootstrap {
    @Bean
    public CommandLineRunner ensureAdmin(UserRepository userRepository) {
        return args -> {
            // logic runs at startup!
        };
    }
}
````
> Here, ensureAdmin is a factory method for a CommandLineRunner bean that will perform logic at startup.

[Back to Table of Contents](#table-of-contents)

---

## 4. Controller Pattern
#### How it works:

- Controllers receive HTTP requests, parse inputs, call appropriate business logic or repositories, 
and return responses.

- Annotated with @RestController and mapped with @RequestMapping.

#### Example:

````
@RestController
@RequestMapping("/products")
public class ProductController {
    @GetMapping
    public List<ProductDTO> getAllProducts() {
        // Fetches and returns product data
    }
}
````

[Back to Table of Contents](#table-of-contents)

---

## 5. Repository Pattern
#### How it works:

- A repository interface abstracts all database access; this business logic doesn’t know (or care) 
how the DB works behind the scenes.

- Spring Data auto-implements these interfaces for you.

#### Example (ProductRepository):

````
public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByUserId(String userId); // custom finder
}
````
> In this controller/service, you can call productRepository.findByUserId(userId)—no manual queries!

[Back to Table of Contents](#table-of-contents)

---

## 6. DTO (Data Transfer Object) Pattern
#### How it works:

- DTOs are simple Java objects used to transfer data without exposing this internal domain models 
(especially sensitive data).

- You map data from entities to DTOs for sending to clients.

#### Example (UserDTO):

````
public class UserDTO {
    private String id;
    private String name;
    private String email;
    // ... no password!
    // getters (and possibly setters)
}
````
> When a User is returned from the repository, you map it to a UserDTO before sending to the client, 
> so sensitive fields like passwords stay hidden.

[Back to Table of Contents](#table-of-contents)

---

## 7. Strategy Pattern (Filter, Exception Handler)
#### How it works:

- Lets you swap in different "strategies" for a task at runtime.

- In this project: Authentication/authorization logic is plugged into the request chain as a filter, 
and exception handling maps errors to specific strategies (methods) for different error cases.

#### Example (JwtAuthenticationFilter):

````
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // ...
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
    FilterChain filterChain) throws ServletException, IOException {
        // JWT check logic here!
        filterChain.doFilter(request, response);
    }
}
````
#### Example (GlobalExceptionHandler):

````
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Object> handleMongoDuplicateKey(DuplicateKeyException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(Map.of("status", "error", "message", "Resource already exists."));
    }
}
````
> Each exception maps to a different handler—this is the Strategy pattern for error handling!

[Back to Table of Contents](#table-of-contents)

---

## Summary Table: Concrete Patterns in this Project
| Pattern	              | Project Files & Examples	                                                | Benefit                                 |
|-----------------------|--------------------------------------------------------------------------|-----------------------------------------|
| Dependency Injection	 | @Autowired fields everywhere (UserController, etc.)	                     | Loose coupling/testability              |
| Singleton	            | All beans (controllers, repos, filters, etc. are singletons by default)	 | Efficiency, shared resources            |
| Factory Method	       | @Bean methods in AdminBootstrap and SecurityConfig	                      | Custom bean creation                    |
| Controller	           | UserController, ProductController (@RestController)	                     | API endpoint handling                   |
| Repository	           | UserRepository, ProductRepository (extends MongoRepository)	             | Easy and safe DB access                 |
| DTO	                  | UserDTO, ProductDTO, LoginResponseDTO	                                   | Secure data transfer                    |
| Strategy	             | JwtAuthenticationFilter, GlobalExceptionHandler	                         | Interchangeable auth and error handling |

[Back to Table of Contents](#table-of-contents)

---