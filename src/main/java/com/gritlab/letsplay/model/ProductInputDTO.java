// For Product Input (Creating & updating products)

package com.gritlab.letsplay.model;

import jakarta.validation.constraints.*;

public class ProductInputDTO {

    @NotBlank
    @Size(min = 2, max = 64)
    @Pattern(regexp = ".*(?:[a-zA-Z].*){3,}.*", message = "Name must contain at least 3 letters.")
    private String name;

    @NotBlank
    @Size(max = 225)
    private String description;

    @NotNull
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
