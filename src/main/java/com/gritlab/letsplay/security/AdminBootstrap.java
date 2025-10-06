package com.gritlab.letsplay.security;

import com.gritlab.letsplay.model.*;
import com.gritlab.letsplay.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AdminBootstrap {
    @Bean
    public CommandLineRunner ensureAdmin(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByRole("admin").isEmpty()) {

                User admin = new User();
                admin.setName("SuperAdmin");
                admin.setEmail("admin@example.com");
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                admin.setPassword(encoder.encode("Superadmin123!"));
                admin.setRole("admin");
                userRepository.save(admin);
            }
        };
    }
}

