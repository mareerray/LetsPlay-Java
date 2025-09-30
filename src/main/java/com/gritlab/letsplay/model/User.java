package com.gritlab.letsplay.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String name;
    private String email;
    private String password;
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
    public String getPassword () {
        return password;
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
    public void setPassword (String password) {
        this.password = password;
    }
    public void setRole (String role) {
        this.role = role;
    }
}

/*
The @Id Annotation
Required for MongoDB: It marks the primary key field for your user documents.
 */


/*
public String getPasswordMasked() {
    if (password == null) return "";
    return "*".repeat(password.length());
}
 */