// Used for accepting user registration requests
// (includes password, validation annotations, usually omits id/role).
package com.gritlab.letsplay.model;

import jakarta.validation.constraints.*;

public class UserRegistrationDTO {
    @NotBlank
    @Pattern(regexp = ".*(?:[a-zA-Z].*){3,}.*", message = "Name must contain at least 3 letters.")
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8)
    private String password;

    private String role;

    // Getters
    public String getName () {
        return name;
    }
    public String getEmail () {
        return email;
    }
    public String getPassword () {
        return password;
    }
    public String getRole() { return role; }

    // Setters
    public void setName (String name) {
        this.name = name;
    }
    public void setEmail (String email) {
        this.email = email;
    }
    public void setPassword (String password) {
        this.password = password;
    }
    public void setRole (String role) { this.role = role; }
}
