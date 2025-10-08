package com.gritlab.letsplay.controller;

import com.gritlab.letsplay.exception.GlobalExceptionHandler.*;
import org.springframework.security.authentication.BadCredentialsException;
import com.gritlab.letsplay.model.*;
import com.gritlab.letsplay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public record LoginResponseDTO(String token, String message) {
    }
    /*
    The traditional Java class structure for above LoginResponseDTO
    public static class LoginResponseDTO {
        private final String token;
        private final String message;

        public LoginResponseDTO(String token, String message) {
            this.token = token;
            this.message = message;
        }
        public String getToken() { return token; }
        public String getMessage() { return message; }
    }
     */

    // REGISTER endpoint (409 Conflict if duplicate emails) ===================================================================== //
    @PostMapping("/register")
    public String registerUser(@Valid @RequestBody UserRegistrationDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new UserAlreadyExistsException();
        }
        // Passwords are always hashed before saving.
        String hashedPassword = passwordEncoder.encode(userDTO.getPassword());

        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(hashedPassword);
        user.setRole("user");  // Always force "user" here as default

        userRepository.save(user);
        return "Registration successful!";
    }

    // LOGIN endpoint (401 Unauthorized if fields are not matched) ========================================== //
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        System.out.println("[DEBUG] Entered UserController.login for " + loginDTO.getEmail());

        Optional<User> userOpt = userRepository.findByEmail(loginDTO.getEmail());
        User user = userOpt
                .orElseThrow(() -> new BadCredentialsException("Invalid authentication credentials."));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid authentication credentials.");
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
// -------------------------- Role: User ------------------------------------------------- //
    // ---- GET own profile (404 Not Found if not exist) ---- //
    @GetMapping("/me")
    public UserDTO getMyProfile(Authentication auth) {
        String userEmail = auth.getName();
        Optional<User> userOpt = userRepository.findByEmail(userEmail);
        User user = userOpt
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
        return toDTO(user);
    }

    // ---- UPDATE own profile (404 Not Found if not exist, 400 Bad Request if bad arguments) ---- //
    @PutMapping("/me")
    public ResponseEntity<Object> updateMyProfile(
            @Valid @RequestBody UserUpdateDTO userDTO,
            Authentication auth) {

        String userEmail = auth.getName(); // usually the username/email
        Optional<User> userOpt = userRepository.findByEmail(userEmail);
        User user = userOpt
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        boolean updated = false;
        boolean passwordChanged = false;

        // Only update name if provided
        if (userDTO.getName() != null && !userDTO.getName().isEmpty()) {
            user.setName(userDTO.getName());
            updated = true;
        }

        // Only update password if present and not empty
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(userDTO.getPassword());
            user.setPassword(hashedPassword);
            updated = true;
            passwordChanged = true;
        }

        if (!updated) {
            throw new IllegalArgumentException("No fields provided to update.");
        }

        user = userRepository.save(user);

        if (passwordChanged) {
            return ResponseEntity.ok(Map.of(
                    "user", toDTO(user),
                    "message", "Password changed successfully."
            ));
        } else {
            return ResponseEntity.ok(toDTO(user));
        }
    }

    // ---- DELETE own profile (404 Not Found if not exist) ---- //
    @DeleteMapping("/me")
    public String deleteOwnProfile(Authentication auth) {
        String userEmail = auth.getName();

        Optional<User> userOpt = userRepository.findByEmail(userEmail);
        User user = userOpt
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        userRepository.deleteById(user.getId());
        return "User deleted";
    }

// -------------------------- Role: Admin ONLY ------------------------------------------------- //
    // **** GET all users **** //
    @PreAuthorize("hasRole('admin')")
    @GetMapping
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::toDTO).toList();
        // returning a list of DTOs from a list of users
    }

    // **** GET user by id (404 Not Found if not exist) **** //
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable String id) {
        Optional<User> userOpt = userRepository.findById(id);
        return userOpt.map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    // **** CREATE user (409 Conflict if duplicate emails) **** //
    @PreAuthorize("hasRole('admin')")
    @PostMapping
    public UserDTO createUser(
            @Valid @RequestBody UserRegistrationDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new UserAlreadyExistsException();
        }
        // Hash plaintext password from registration DTO
        String hashedPassword = passwordEncoder.encode(userDTO.getPassword());

        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(hashedPassword);
        user.setRole("user"); // Always default 'user'

        user = userRepository.save(user);
        return toDTO(user);
    }

    // **** UPDATE user by id (404 Not Found if not exist, 400 Bad Request if bad arguments) **** //
    @PreAuthorize("hasRole('admin')")
    @PutMapping("/{id}")
    public UserDTO updateUser(
            @PathVariable String id,
            @RequestBody UserUpdateDTO userDTO) {

        Optional<User> userOpt = userRepository.findById(id);
        User user = userOpt
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        boolean updated = false;

        // Only update name if provided
        if (userDTO.getName() != null && !userDTO.getName().isEmpty()) {
            user.setName(userDTO.getName());
            updated = true;
        }

        // Only update password if provided
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            updated = true;
        }

        if (!updated) {
            throw new IllegalArgumentException("No fields provided to update.");
        }

        user = userRepository.save(user);
        return toDTO(user);
    }

    // **** DELETE user by id (404 Not Found if not exist) **** //
    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable String id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found.");
        }

        userRepository.deleteById(id);
        return "User deleted";
    }

    // -------------------------- Helper function ------------------------------------------------- //
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