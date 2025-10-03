package com.gritlab.letsplay.controller;

import com.gritlab.letsplay.exception.GlobalExceptionHandler.*;
import org.springframework.security.authentication.BadCredentialsException;
import com.gritlab.letsplay.model.*;
import com.gritlab.letsplay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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

    // REGISTER endpoint ===================================================================== //
    @PostMapping("/register")
    public String registerUser(@Valid @RequestBody UserRegistrationDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new UserAlreadyExistsException();
        }
        // Passwords are always hashed before saving.
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(userDTO.getPassword());

        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(hashedPassword);
        user.setRole("user");  // Always force "user" here

        userRepository.save(user);
        return "Registration successful!";
    }

    // LOGIN endpoint ===================================================================== //
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        System.out.println("[DEBUG] Entered UserController.login for " + loginDTO.getEmail());
        User user = userRepository.findByEmail(loginDTO.getEmail());
        if (user == null) {
            throw new BadCredentialsException("Invalid authentication credentials.");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(loginDTO.getPassword(), user.getPassword())) {
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
    // ---- GET own profile ---- //
    @GetMapping("/me")
    public UserDTO getMyProfile(Authentication auth) {
        String userEmail = auth.getName();
        User user = userRepository.findByEmail(userEmail);
        return toDTO(user);
    }

    // ---- UPDATE own profile ---- //
    @PutMapping("/me")
    public UserDTO updateMyProfile(
            @Valid @RequestBody UserRegistrationDTO userDTO,
            Authentication auth) {

        // Get email from JWT to ensure only the user can update their data.
        String userEmail = auth.getName(); // usually the username/email
        User user = userRepository.findByEmail(userEmail);

        if (user == null) {
            // User is not allowed to update anyone else
            throw new ResourceNotFoundException("User not found");
        }

        boolean updated = false;

        // Only update name if provided
        if (userDTO.getName() != null && !userDTO.getName().isEmpty()) {
            user.setName(userDTO.getName());
            updated = true;
        }

        // Never change email through profile update (for safety)
        if (userDTO.getEmail() != null && !userDTO.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email update is not allowed.");
        }

        // Only update password if present and not empty
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(userDTO.getPassword()));
            updated = true;
        }

        // Forbid role change
        if (userDTO.getRole() != null && !userDTO.getRole().isEmpty()) {
            throw new IllegalArgumentException("Role update is not allowed.");
        }

        if (!updated) {
            // No update fields provided
            throw new IllegalArgumentException("No fields provided to update.");
        }

        user = userRepository.save(user);
        return toDTO(user);
    }

    // ---- DELETE own profile ---- //
    @DeleteMapping("/me")
    public String deleteOwnProfile(Authentication auth) {
        String userEmail = auth.getName();
        User user = userRepository.findByEmail(userEmail);

        if (user == null) {
            throw new ResourceNotFoundException("User not found.");
        }
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

    // **** GET user by id **** //
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable String id) {
        Optional<User> userOpt = userRepository.findById(id);
        return userOpt.map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    // **** CREATE user **** //
    @PreAuthorize("hasRole('admin')")
    @PostMapping
    public UserDTO createUser(
            @Valid @RequestBody UserRegistrationDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new UserAlreadyExistsException();
        }
        // Passwords are always hashed before saving.
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(userDTO.getPassword());

        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(hashedPassword);

        // Only allow specific roles for security
        if (!"admin".equals(userDTO.getRole()) && !"user".equals(userDTO.getRole())) {
            throw new InvalidRoleException("Role must be either 'admin' or 'user'.");
        }
        user.setRole(userDTO.getRole());

        user = userRepository.save(user);
        return toDTO(user);
    }

    // **** UPDATE user by id (404 if not exist, update fields) **** //
    @PreAuthorize("hasRole('admin')")
    @PutMapping("/{id}")
    public UserDTO updateUser(
            @PathVariable String id,
            @RequestBody UserDTO userDTO) {

        Optional<User> userOpt = userRepository.findById(id);
        User user = userOpt
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        boolean updated = false;

        // Only update name if provided
        if (userDTO.getName() != null && !userDTO.getName().isEmpty()) {
            user.setName(userDTO.getName());
            updated = true;
        }

        // Never change email through profile update (for safety)
        if (userDTO.getEmail() != null && !userDTO.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email update is not allowed.");
        }

        // Allow admin to update role, but only to 'user' or 'admin'
        if (userDTO.getRole() != null && !userDTO.getRole().isEmpty()) {
            if (!userDTO.getRole().equals("user") && !userDTO.getRole().equals("admin")) {
                throw new IllegalArgumentException("Role must be either 'admin' or 'user'.");
            }
            user.setRole(userDTO.getRole());
            updated = true;
        }

        if (!updated) {
            throw new IllegalArgumentException("No fields provided to update.");
        }

        user = userRepository.save(user);
        return toDTO(user);
    }

    // **** DELETE user by id (404 if not exist) **** //
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