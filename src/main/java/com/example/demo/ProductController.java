 package com.example.demo;

 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.http.HttpStatus;
 import org.springframework.http.ResponseEntity;
 import org.springframework.web.bind.annotation.*;
 import java.util.List;
 import java.util.Optional;
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
     public ResponseEntity<ProductDTO> getProductById(@PathVariable String id) {
         Optional<Product> productOpt = productRepository.findById(id);
         return productOpt.map(p -> ResponseEntity.ok(toDTO(p))).orElseGet(() -> ResponseEntity.notFound().build());
     }

     // -------------------------- Need auth ------------------------------------------------- //
     // === CREATE product === //
     @PostMapping
     public ResponseEntity<ProductDTO> createProduct(
             @Valid @RequestBody ProductInputDTO input,
             Authentication auth) {
         // Get email from JWT to ensure only the authenticated user can create product.
         String userEmail = auth.getName(); // usually the username/email
         User user = userRepository.findByEmail(userEmail);


         if (user == null) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
         }
         String userId = user.getId();

         Product product = new Product();
         product.setName(input.getName());
         product.setDescription(input.getDescription());
         product.setPrice(input.getPrice());
         product.setUserId(userId);

         Product saved = productRepository.save(product);
         return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(saved));
     }


     // === UPDATE product (ADMIN & product owner) === //
     @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable String id,
            @Valid @RequestBody ProductInputDTO input,
            Authentication auth) {

         Optional<Product> optionalProduct = productRepository.findById(id);

         // Checks for missing product and returns HTTP 404 if not found.
         if (optionalProduct.isEmpty()) {
             return ResponseEntity.notFound().build();
         }

         Product product = optionalProduct.get();

         // Looks up the authenticated user by email and retrieves their id and role.
         String userEmail = auth.getName();
         User user = userRepository.findByEmail(userEmail);

         if (user == null) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
         }

         // Allows the operation only if the authenticated user
         // is either the owner of the product or an admin.
         if (!product.getUserId().equals(user.getId()) && !"admin".equalsIgnoreCase(user.getRole())) {
             return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
         }

         product.setName(input.getName());
         product.setDescription(input.getDescription());
         product.setPrice(input.getPrice());
         Product updatedProduct = productRepository.save(product);
         return ResponseEntity.ok(toDTO(updatedProduct));
     }


     // === DELETE product (ADMIN & product owner) === //
     @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(
            @PathVariable String id,
            Authentication auth) {
         Optional<Product> optionalProduct = productRepository.findById(id);

         // Checks for missing product and returns HTTP 404 if not found.
         if (optionalProduct.isEmpty()) {
             return ResponseEntity.notFound().build();
         }

         Product product = optionalProduct.get();

         // Looks up the authenticated user by email and retrieves their id and role.
         String userEmail = auth.getName();
         User user = userRepository.findByEmail(userEmail);

         if (user == null) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
         }

         // Allows the operation only if the authenticated user
         // is either the owner of the product or an admin.
         if (!product.getUserId().equals(user.getId()) && !"admin".equalsIgnoreCase(user.getRole())) {
             return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
         }

         productRepository.delete(product);
         return ResponseEntity.ok("Product deleted");
     }

     // === GET product by user id === //
     @GetMapping("/me")
     public ResponseEntity<List<ProductDTO>> getMyProducts(Authentication auth) {
         // Looks up the authenticated user by email and retrieves their id and role.
         String userEmail = auth.getName();
         User user = userRepository.findByEmail(userEmail);

         if (user == null) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
         }

         List<Product> products = productRepository.findByUserId(user.getId());
         List<ProductDTO> productDTOList = products.stream().map(this::toDTO).toList();
         return ResponseEntity.ok(productDTOList);
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


