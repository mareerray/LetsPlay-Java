package com.gritlab.letsplay.security;

import com.gritlab.letsplay.model.*;
import com.gritlab.letsplay.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class AdminBootstrap {
    @Bean
    @ConditionalOnProperty(name = "admin.bootstrap.enabled", havingValue = "true", matchIfMissing = false)
    public CommandLineRunner ensureAdmin(UserRepository users, Environment env) {
        return args -> {
            String email = env.getProperty("admin.email");
            String hash = env.getProperty("admin.password-hash");

            if (email == null || email.isBlank() || hash == null || hash.isBlank()) {
                throw new IllegalStateException("admin.email and admin.password-hash must be set");
            }

            if (users.existsByEmail(email)) {
                return; // idempotent: nothing to do, admin already there
            }

            User admin = new User();
            admin.setName("SuperAdmin");
            admin.setEmail(email);
            admin.setPassword(hash); // already encoded
            admin.setRole("admin");
            users.save(admin);
        };
    }
}

