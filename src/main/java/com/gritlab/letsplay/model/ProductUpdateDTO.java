package com.gritlab.letsplay.model;

import jakarta.validation.constraints.*;

public class ProductUpdateDTO {

    @Pattern(regexp = ".*(?:[a-zA-Z].*){3,}.*", message = "Name must contain at least 3 letters.")
    @Size(min = 2, max = 64)
    private String name;

    @Size(max = 225)
    private String description;

    @DecimalMin("0.0")
    private Double price;


    // Getters
    public String getName () {
        return name;
    }
    public String getDescription () {
        return description;
    }
    public Double getPrice () {
        return price;
    }

    // Setters
    public void setName (String name) {
        this.name = name;
    }
    public void setDescription (String description) {
        this.description = description;
    }
    public void setPrice (Double price) {
        this.price = price;
    }
}
