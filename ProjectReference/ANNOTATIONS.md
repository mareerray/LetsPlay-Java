# Annotation Reference

## Table of Annotated keyword

 [@Autowired](#autowired) , [@Bean](#bean) , [@Configuration](#configuration) , [@ControllerAdvice](#controlleradvice) , [@DecimalMin](#decimalMin) , [@DeleteMapping](#getmapping) , [@Document](#document) ,[@Email](#email) ,[@EnableMethodSecurity](#enablemethodsecurity) , [@EnableWebSecurity](#enablewebsecurity) , 
 
[@ExceptionHandler](#exceptionhandler) , [@GetMapping](#getmapping) , [@Id](#id) , [@NotBlank](#notblank) , [@NotNull](#notnull) ,[@Override](#override) ,[@PathVariable](#pathvariable) , [@Pattern](#pattern) , [@PostMapping](#getmapping) , [@PreAuthorize](#preauthorize) , 

 [@PutMapping](#putmapping) , [@RequestBody](#requestbody) ,[@RequestMapping](#requestmapping) , [@RestController](#restcontroller) , [@Size](#size) , [@Valid](#valid) , [@Value](#value) 

- [Quick note](#quick-note)


> This is a categorized table of all the annotations used in this project, with their descriptions and usage examples:

| Annotation	                                               | Category	            | Description	                                                                                                                                                                                                                           | Example (from your code)                                                                                                                                                     |
|-----------------------------------------------------------|----------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| <a id="document"></a>@Document                            | Spring Data MongoDB	 | Marks a class as a MongoDB document (a collection). Assigns the class to a specific collection in MongoDB.	                                                                                                                            | @Document(collection = "products") @Document(collection = "users")                                                                                                           |
| <a id="id"></a>@Id	                                       | Spring Data MongoDB	 | Indicates the primary key (unique identifier) for a MongoDB document.                                                                                                                                                                  | 	@Id private String id;                                                                                                                                                      |
| <a id="controlleradvice"></a>@ControllerAdvice	           | Spring/Web	          | Used to define global exception handlers, cross-cutting concerns across all controllers.                                                                                                                                               | 	@ControllerAdvice                                                                                                                                                           |
| <a id="exceptionhandler"></a>@ExceptionHandler            | 	Spring/Web	         | Used in a class marked with @ControllerAdvice to handle specific exception types.	                                                                                                                                                     | @ExceptionHandler(DuplicateKeyException.class)                                                                                                                               |
| <a id="restcontroller"></a>@RestController	               | Spring Web	          | Specialized version of @Controller that adds @ResponseBody to all handler methods, returning JSON by default.	                                                                                                                         | @RestController                                                                                                                                                              |
| <a id="requestmapping"></a>@RequestMapping	               | Spring Web	          | Used to map HTTP requests to handler methods/class-level base path configuration.	                                                                                                                                                     | @RequestMapping("users") @RequestMapping("products")                                                                                                                         |
| <a id="getmapping"></a>@GetMapping / @PostMapping / etc.	 | Spring Web	          | Shortcuts for specific HTTP verbs to methods within controllers (GET, POST, PUT, DELETE).	                                                                                                                                             | @GetMapping("me") @PostMapping("login")                                                                                                                                      |
| <a id="pathvariable"></a>@PathVariable	                   | Spring Web	          | Bind a method parameter to a URI template variable.	                                                                                                                                                                                   | @GetMapping("{id}") public UserDTO getUser(@PathVariable String id)                                                                                                          |
| <a id="autowired"></a>@Autowired	                         | Spring	              | Marks a field or setter for dependency injection by Spring’s IoC container.	                                                                                                                                                           | @Autowired private UserRepository userRepository;                                                                                                                            |
| <a id="value"></a>@Value	                                 | Spring	              | Inject a value from the application properties/environment into a field.	                                                                                                                                                              | @Value("jwt.secret") private String jwtSecret;                                                                                                                               |
| <a id="valid"></a>@Valid	                                 | Jakarta Validation	  | Triggers validation on an object being passed as an argument (usually DTOs in controller methods).	                                                                                                                                    | public String registerUser(@Valid @RequestBody UserRegistrationDTO userDTO)                                                                                                  |
| <a id="requestbody"></a>@RequestBody	                     | Spring Web	          | Indicates a method parameter should be bound to the body of the HTTP request.	                                                                                                                                                         | public String registerUser(@Valid @RequestBody UserRegistrationDTO userDTO)                                                                                                  |
| <a id="bean">@Bean	                                       | Spring	              | Marks a method as producing a bean to be managed by the Spring context.	                                                                                                                                                               | @Bean public SecurityFilterChain filterChain(HttpSecurity http)                                                                                                              |
| <a id="configuration">@Configuration	                     | Spring	              | Indicates a configuration class defining beans or settings.	                                                                                                                                                                           | @Configuration                                                                                                                                                               |
| <a id="enablewebsecurity">@EnableWebSecurity	             | Spring Security	     | Enables Spring Security’s web security support.	                                                                                                                                                                                       | @EnableWebSecurity                                                                                                                                                           |
| <a id="enablemethodsecurity">@EnableMethodSecurity	       | Spring Security	     | Enables Spring Security’s method-level security annotations.	                                                                                                                                                                          | @EnableMethodSecurity                                                                                                                                                        |
| <a id="preauthorize">@PreAuthorize	                       | Spring Security	     | Method-level security to restrict access based on Spring Expression Language (SpEL) conditions/roles.	                                                                                                                                 | @PreAuthorize("hasRole('admin')")                                                                                                                                            |
| <a id="override"></a>@Override	                           | Java Language	       | Instructs the compiler that the method is intended to override a method from a superclass/interface. Helps catch errors at compile time if the method does not actually override anything (such as typos or wrong method signatures).	 | @Override<br>protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException { ... } |

[Back to Table of Annotated keyword](#table-of-annotated-keyword)

---

### Validation Annotations (Jakarta Validation / Bean Validation):

| Annotation	                                  | Description	                                                               | Example                                                                                        |
|----------------------------------------------|----------------------------------------------------------------------------|------------------------------------------------------------------------------------------------|
| <a id="notblank">@NotBlank	                  | Ensures the annotated string is not null and trimmed length > 0.	          | @NotBlank private String name;                                                                 |
| <a id="notnull">@NotNull	                    | Ensures the annotated field is not null.	                                  | @NotNull private Double price;                                                                 |
| <a id="size">@Size(min=2, max=64)	           | Ensures the length of the annotated field is within the specified bounds.	 | @Size(min=2, max=64) private String name;                                                      |
| <a id="email">@Email	                        | Ensures the string matches email format.	                                  | @Email @NotBlank private String email;                                                         |
| <a id="pattern">@Pattern	                    | Ensures the string matches the given regex pattern.	                       | @Pattern(regexp=".{3,}", message="Name must contain at least 3 letters.") private String name; |
| <a id="decimalmin">@DecimalMin(value="0.0")	 | Ensures the number is at least the given minimum.	                         | @DecimalMin("0.0") private Double price;                                                       |


| Annotation	 | Applies to	 | Checks for	                  | Allow ""?	 | Allow " "? |
|-------------|-------------|------------------------------|------------|------------| 
| @NotNull	   | Any type	   | Not null	                    | Yes	       | Yes        |
| @NotEmpty	  | String/Col	 | Not null, length > 0	        | No	        | Yes        |
| @NotBlank	  | String	     | Not null, trimmed length > 0 | 	No	       | No         |

[Back to Table of Annotated keyword](#table-of-annotated-keyword)

---

### Quick Note

#### 1. Validation annotations **(@Email, @NotBlank, @Size, etc.)** 
are typically placed on fields in DTO/model classes for **automatic request validation**. 
Spring/Java/Jakarta Validation will enforce these rules when a request comes in with annotated objects. 
- Important: These validations only trigger when the object is used in a controller method parameter annotated with @Valid.

---

#### 2. Mapping annotations (@GetMapping, @PostMapping, @PutMapping, @DeleteMapping)
are placed directly above controller methods to handle specific HTTP request types and URLs.
- Important: The path inside the annotation is relative to the @RequestMapping at the class level, if present.

--- 

#### 3. @SpringBootApplication is a meta-annotation for your main application class. It internally combines:
- @Configuration (declares a configuration class)
- @EnableAutoConfiguration (enables Spring Boot’s auto-configuration)
- @ComponentScan (component scanning on the package of the class)

- Important: Use this annotation only once, and only on your main entry class (where the main() method is located).

---

#### 4. @Override (Java built-in): Used on methods that override superclass/interface methods, improves clarity and prevents mistakes.
- Important: Use @Override whenever you are overriding methods for better code safety and readability.

[Back to Table of Annotated keyword](#table-of-annotated-keyword)

---

