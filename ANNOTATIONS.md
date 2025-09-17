# Annotation

| Annotation| Purpose/Effect                                                             | 	Example                                                                                                 |
| ---------- |----------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------|
|@RestController| 	Marks class as REST API, auto-converts response to JSON	                  | @RestController public class UserController { }                                                          |
|@RequestMapping| 	Maps base URL for API endpoints	                                          | @RequestMapping("/users")                                                                                |
|@Autowired| 	Injects Spring-managed bean automatically| 	@Autowired UserRepository repo;                                                                         |
|@Value |	Injects config value from application.properties| 	@Value("${jwt.secret}") String secret;                                                                  |
|@PostMapping|	Maps HTTP POST requests to a specific method | 	@PostMapping("/login")                                                                                  |
|@GetMapping |	Maps HTTP GET requests to a method | 	@GetMapping("/profile")                                                                                 |
|@PutMapping |	Maps HTTP PUT requests to a method | 	@PutMapping("/edit")                                                                                    |
|@DeleteMapping |	Maps HTTP DELETE requests to a method| 	@DeleteMapping("/delete")                                                                               |
|@RequestBody|	Maps incoming HTTP body (JSON) to object| 	@RequestBody UserDTO dto                                                                                |
|@Valid |	Applies bean validation to DTO or parameters| 	@Valid @RequestBody UserDTO dto                                                                         |
|@Email |	Validates field contains a valid email format| 	@Email private String email;                                                                            |
|@NotBlank	|Validates field is not null, empty, or whitespace only| 	@NotBlank private String password;                                                                      |
|@Size|	Restricts size/length of string, collection, array| 	@Size(min=8, max=20) private String password;                                                           |
|@Id	|Marks field as entity’s primary key| 	@Id private Long id;                                                                                    |
|@Configuration |	Marks class as source of bean definitions/config | 	@Configuration public class AppConfig { }                                                               |
|@EnableWebSecurity	|Enables Spring Security for web applications| 	@EnableWebSecurity public class SecurityConfig { }                                                      |
|@Override |	Indicates a method overrides superclass method| 	@Override public void run() { }                                                                         |
|@SpringBootApplication |	Main entry annotation; enables auto-configuration, etc.| 	@SpringBootApplication public class DemoApp { }                                                         |
 |@EnableMethodSecurity | Enables method-level security annotations in your app | On your @Configuration class: @EnableMethodSecurity                                                      |
| @PreAuthorize	| Checks a SpEL expression before method execution (deny access if not met) | 	@PreAuthorize("hasRole('ADMIN')") Method can only be called if the user has role ADMIN.                 |
| @PostAuthorize |	Checks a SpEL expression after method execution (can block access to result) | 	@PostAuthorize("returnObject.owner == authentication.name") (Allow if returned object is owned by user) |
 | @Secured	| Simpler check:Only allows users with any of the listed roles; does not use SpEL | 	@Secured("ROLE_ADMIN")                                                                                  |
User must have "ROLE_ADMIN"
### Quick Note
- Validation annotations (@Email, @NotBlank, @Size) are typically placed on fields in DTO/model classes for automatic request validation.

- Mapping annotations (@GetMapping, @PostMapping, @PutMapping, @DeleteMapping) are placed directly above controller methods to handle specific HTTP request types.

- @SpringBootApplication combines several crucial annotations for your main class: @Configuration, @EnableAutoConfiguration, and @ComponentScan.



> **@Email** checks the email follows standard email pattern example@domain.com .

> **@NotBlank** ensures the field contains at least one non-whitespace character.

> **@RestController** marks the class as a RESTful web controller, combining @Controller and @ResponseBody to automatically convert returned objects to JSON.

> **@RequestMapping** defines the base URL mapping for all methods within the controller.

> **@Autowired** automatically injects a Spring-managed bean (e.g., repository, service) into the class.

> **@Value** injects a value from the application.properties file into a variable.

> **@PostMapping** maps HTTP POST requests to a specific method; used for endpoints handling data creation or submission.

> **@RequestBody** converts incoming JSON in the HTTP request body into a Java object.

> **@Valid** triggers bean validation (e.g., checks for @NotNull, @Email) on the annotated object.