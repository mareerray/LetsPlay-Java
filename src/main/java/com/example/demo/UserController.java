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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.SecurityFilterChain;



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
}