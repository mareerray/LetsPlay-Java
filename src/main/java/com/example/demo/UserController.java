package com.example.demo;
import com.example.demo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.Authentication;



@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public static class LoginResponseDTO {
        private String token;
        private String message;

        public LoginResponseDTO(String token, String message) {
            this.token = token;
            this.message = message;
        }
        public String getToken() { return token; }
        public String getMessage() { return message; }
    }

    // REGISTER endpoint
    @PostMapping("/register")
    public String registerUser(@Valid @RequestBody UserRegistrationDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            return "Email already exists!";
        }
        // Passwords are always hashed before saving.
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(userDTO.getPassword());

        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(hashedPassword);
        user.setRole("user");

        userRepository.save(user);
        return "Registration successful!";
    }

    // LOGIN endpoint
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        System.out.println("[DEBUG] Entered UserController.login for " + loginDTO.getEmail());
        User user = userRepository.findByEmail(loginDTO.getEmail());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDTO(null, "Invalid credentials!"));
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDTO(null, "Invalid credentials!"));
        }

        // Token details
        long expirationMs = 86400000; // 1 day
        String jwt = Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole())
                .claim("userId", user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();

        return ResponseEntity.ok(new LoginResponseDTO(jwt, "Login successful!"));
    }

    // GET all users (use DTO, no passwords)
    @GetMapping
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::toDTO).toList();
        // returning a list of DTOs from a list of users
    }

    // GET single user by id with 404 and DTO
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String id) {
        Optional<User> userOpt = userRepository.findById(id);
        return userOpt.map(user -> ResponseEntity.ok(toDTO(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    // CREATE user (no duplicate email, password hashing, and return 409 if conflict or 201 if created)
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserRegistrationDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        // Passwords are always hashed before saving.
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(userDTO.getPassword());

        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(hashedPassword);
        user.setRole("user");
        user = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(user));
    }

    // UPDATE user by id (404 if not exist, update fields)
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable String id,
            @Valid @RequestBody UserRegistrationDTO userDTO,
            Authentication auth) {

        // Get email from JWT to ensure only the user can update their data.
        String userEmail = auth.getName(); // usually the username/email
        User authenticatedUser = userRepository.findByEmail(userEmail);

        if (authenticatedUser == null || !authenticatedUser.getId().equals(id)) {
            // User is not allowed to update anyone else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());

        // Only update password if present and not empty
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(userDTO.getPassword()));
        }

        user = userRepository.save(user);
        return ResponseEntity.ok(toDTO(user));
    }

    // DELETE user by id (404 if not exist)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id, Authentication auth) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build(); // return 404 if id not found
        }
        String userEmail = auth.getName();
        User authenticatedUser = userRepository.findByEmail(userEmail);

        if (authenticatedUser == null || !authenticatedUser.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();  // return 403 if not the owner
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok("User deleted"); // return 200 Ok
        // or return ResponseEntity.noContent().build(); // return 204 No Content
    }

    // Helper method to convert entity to DTO
    private UserDTO toDTO (User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }
}