// Used for accepting user registration requests
// (includes password, validation annotations, usually omits id/role).
package com.example.demo;

import javax.validation.constraints.*;

public class UserRegistrationDTO {
    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8)
    private String password;

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
}
