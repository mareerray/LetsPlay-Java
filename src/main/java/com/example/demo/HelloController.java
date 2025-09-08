package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // tells Spring that this class handles web requests.
public class HelloController {
    @GetMapping("/hello") // connects the /hello path to your hello() method
    public String hello() {
        return "Hello, world!";  // The method returns a simple plain-text response.
    }
}
