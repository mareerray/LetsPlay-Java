package com.example.demo;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
    List<User> findByRole(String role);
}
