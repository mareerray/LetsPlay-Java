package com.gritlab.letsplay.model;

import jakarta.validation.constraints.*;

public class UserLoginDTO {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    // Getters
    public String getEmail () {
        return email;
    }
    public String getPassword () {
        return password;
    }

    // Setters
    public void setEmail (String email) {
        this.email = email;
    }
    public void setPassword (String password) {
        this.password = password;
    }
}
