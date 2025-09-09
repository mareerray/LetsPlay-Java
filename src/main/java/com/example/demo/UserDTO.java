// Use this UserDTO every time you send user info in your controllers or services.
package com.example.demo;

import org.springframework.data.annotation.Id;

public class UserDTO {
    private String id;
    private String name;
    private String email;
    private String role;

    // Getters
    public String getId () {
        return id;
    }
    public String getName () {
        return name;
    }
    public String getEmail () {
        return email;
    }
    public String getRole () {
        return role;
    }

    // Setters
    public void setId (String id) {
        this.id = id;
    }
    public void setName (String name) {
        this.name = name;
    }
    public void setEmail (String email) {
        this.email = email;
    }
    public void setRole (String role) {
        this.role = role;
    }
}