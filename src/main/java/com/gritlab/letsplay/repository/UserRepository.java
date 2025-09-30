package com.gritlab.letsplay.repository;

import com.gritlab.letsplay.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
    List<User> findByRole(String role);
}
