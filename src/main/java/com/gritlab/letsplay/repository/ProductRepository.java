package com.gritlab.letsplay.repository;

import com.gritlab.letsplay.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    // Custom query methods can be added here if needed (e.g., findByUserId)
    List<Product> findByUserId(String userId);
}