package com.gritlab.letsplay.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Document(collection = "products")
public class Product {
    @Id
    private String id;

    @NotNull @Size(min = 2, max = 64)
    private String name;

    @Size(max = 225)
    private String description;

    @NotNull @DecimalMin("0.0")
    private Double price;

    @NotNull
    private String userId;  // ID of the user who owns the product

    // Getters
    public String getId () {
        return id;
    }
    public String getName () {
        return name;
    }
    public String getDescription () {
        return description;
    }
    public Double getPrice () {
        return price;
    }
    public String getUserId () {
        return userId;
    }

    // Setters
    public void setId (String id) {
        this.id = id;
    }
    public void setName (String name) {
        this.name = name;
    }
    public void setDescription (String description) {
        this.description = description;
    }
    public void setPrice (Double price) {
        this.price = price;
    }
    public void setUserId (String userId) {
        this.userId = userId;
    }

    public static class ProductDTO {

        private String id;
        private String name;
        private String description;
        private Double price;
        private String userId;  // ID of the user who owns the product

        // Getters
        public String getId () {
            return id;
        }
        public String getName () {
            return name;
        }
        public String getDescription () {
            return description;
        }
        public Double getPrice () {
            return price;
        }
        public String getUserId () {
            return userId;
        }

        // Setters
        public void setId (String id) {
            this.id = id;
        }
        public void setName (String name) {
            this.name = name;
        }
        public void setDescription (String description) {
            this.description = description;
        }
        public void setPrice (Double price) {
            this.price = price;
        }
        public void setUserId (String userId) {
            this.userId = userId;
        }

    }
}

/*
The @Id Annotation
Required for MongoDB: It marks the primary key field for your user documents.
 */