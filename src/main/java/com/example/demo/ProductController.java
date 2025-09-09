 package com.example.demo;

 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.web.bind.annotation.*;
 import java.util.List;
 import java.util.Optional;

 @RestController
 @RequestMapping("/products")
 public class ProductController {

     @AutoWired
     private ProductRepository productRepository;

     // GET all products (public)
     @GetMapping
     public List<Product> getAllProducts() {
         return productRepository.findAll();
     }

     // GET single product (public)
     @GetMapping("/{id}")
     public Optional<product> getProductById(@PathVariable String id) {
         return productRepository.findById(id);
     }

     // CREATE product
     @PostMapping
     public Product createProduct(@RequestBody Product product) {
         return productRepository.save(product);
     }

     // UPDATE product
     @PutMapping("/{id}")
    public Product updateProduct(@PathVariable String id,@RequestBody Product product) {
         product.setId(id);
         return productRepository.save(product);
     }

     // DELETE product
     @DeleteMapping("/{id}")
    public Product deleteProduct(@PathVariable String id) {
         return productRepository.deleteById(id);
     }

     // GET product by user id
     @GetMapping("/user/{userId}")
     public List<product> getProductsByUserId(@PathVariable String userId) {
         return productRepository.findByUserId(userId);
     }
 }



/*
Key Features
- Handles full CRUD (Create, Read, Update, Delete) operations for products.
- Public endpoint: /products (for retrieving product list).
- Demo versions for all core REST methods.
 */