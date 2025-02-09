package com.project.system2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(
	exclude = {
		SecurityAutoConfiguration.class,
	}
)
public class System2Application {

	public static void main(String[] args) {
		SpringApplication.run(System2Application.class, args);
	}

}
