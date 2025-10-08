package com.gritlab.letsplay.model;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserUpdateDTO {
    @Pattern(regexp = ".*(?:[a-zA-Z].*){3,}.*", message = "Name must contain at least 3 letters.")
    private String name;

    @Size(min = 8)
    private String password;

    // Getters
    public String getName () {
        return name;
    }
    public String getPassword () {
        return password;
    }

    // Setters
    public void setName (String name) {
        this.name = name;
    }
    public void setPassword (String password) {
        this.password = password;
    }
}
