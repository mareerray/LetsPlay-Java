 package com.gritlab.letsplay.controller;

 import com.gritlab.letsplay.exception.GlobalExceptionHandler.*;
 import com.gritlab.letsplay.model.*;
 import com.gritlab.letsplay.repository.ProductRepository;
 import com.gritlab.letsplay.repository.UserRepository;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.web.bind.annotation.*;
 import java.util.List;
 import org.springframework.security.core.Authentication;
 import javax.validation.Valid;

 @RestController
 @RequestMapping("/products")
 public class ProductController {

     @Autowired
     private UserRepository userRepository;

     @Autowired
     private ProductRepository productRepository;

     // -------------------------- Public access ------------------------------------------------- //
     // GET all products (public)
     @GetMapping
     public List<ProductDTO> getAllProducts() {
         return productRepository.findAll().stream().map(this::toDTO).toList();
     }

     // GET single product (public)
     @GetMapping("/{id}")
     public ProductDTO getProductById(@PathVariable String id) {
         Product product = productRepository.findById(id)
                 .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
         return toDTO(product);
     }

     // -------------------------- Need auth ------------------------------------------------- //
     // === CREATE product === //
     @PostMapping
     public ProductDTO createProduct(
             @Valid @RequestBody ProductInputDTO input,
             Authentication auth) {
         // Get email from JWT to ensure only the authenticated user can create product.
         String userEmail = auth.getName(); // usually the username/email
         User user = userRepository.findByEmail(userEmail);

         if (user == null) {
             throw new UnauthorizedException("User not authenticated.");
         }
         String userId = user.getId();

         Product product = new Product();
         product.setName(input.getName());
         product.setDescription(input.getDescription());
         product.setPrice(input.getPrice());
         product.setUserId(userId);

         Product saved = productRepository.save(product);
         return toDTO(saved);
     }


     // === UPDATE product (ADMIN & product owner) === //
     @PutMapping("/{id}")
    public ProductDTO updateProduct(
            @PathVariable String id,
            @Valid @RequestBody ProductInputDTO input,
            Authentication auth) {

         Product product = productRepository.findById(id)
                 .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

         // Looks up the authenticated user by email and retrieves their id and role.
         String userEmail = auth.getName();
         User user = userRepository.findByEmail(userEmail);

         if (user == null) {
             throw new UnauthorizedException("User not authenticated.");
         }

         // Allows the operation only if the authenticated user
         // is either the owner of the product or an admin.
         if (!product.getUserId().equals(user.getId()) && !"admin".equalsIgnoreCase(user.getRole())) {
             throw new ForbiddenException("You don't have permission to modify this product.");
         }

         product.setName(input.getName());
         product.setDescription(input.getDescription());
         product.setPrice(input.getPrice());
         Product updatedProduct = productRepository.save(product);
         return toDTO(updatedProduct);
     }


     // === DELETE product (ADMIN & product owner) === //
     @DeleteMapping("/{id}")
    public String deleteProduct(
            @PathVariable String id,
            Authentication auth) {
         Product product = productRepository.findById(id)
                 .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

         // Looks up the authenticated user by email and retrieves their id and role.
         String userEmail = auth.getName();
         User user = userRepository.findByEmail(userEmail);

         if (user == null) {
             throw new UnauthorizedException("User not authenticated.");
         }

         // Allows the operation only if the authenticated user
         // is either the owner of the product or an admin.
         if (!product.getUserId().equals(user.getId()) && !"admin".equalsIgnoreCase(user.getRole())) {
             throw new ForbiddenException("You don't have permission to delete this product.");
         }

         productRepository.delete(product);
         return "Product deleted";
     }

     // === GET product by user id === //
     @GetMapping("/me")
     public List<ProductDTO> getMyProducts(Authentication auth) {
         // Looks up the authenticated user by email and retrieves their id and role.
         String userEmail = auth.getName();
         User user = userRepository.findByEmail(userEmail);

         if (user == null) {
             throw new UnauthorizedException("User not authenticated.");
         }

         List<Product> products = productRepository.findByUserId(user.getId());
         return  products.stream().map(this::toDTO).toList();
     }

     // -------------------------- Helper function ------------------------------------------------- //
     // Helper method to convert entity to DTO
     private ProductDTO toDTO (Product product) {
         ProductDTO dto = new ProductDTO();
         dto.setId(product.getId());
         dto.setName(product.getName());
         dto.setDescription(product.getDescription());
         dto.setPrice(product.getPrice());
         dto.setUserId(product.getUserId());
         return dto;
     }
 }


