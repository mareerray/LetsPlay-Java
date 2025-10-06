package com.gritlab.letsplay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LetsPlayApplication {

	public static void main(String[] args) {
		SpringApplication.run(LetsPlayApplication.class, args);
		//System.out.println(new BCryptPasswordEncoder().encode("Superadmin123!"));
	}

}
