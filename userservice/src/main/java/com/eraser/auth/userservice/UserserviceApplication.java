package com.eraser.auth.userservice;

import com.eraser.auth.userservice.domain.Role;
import com.eraser.auth.userservice.domain.User;
import com.eraser.auth.userservice.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class UserserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserserviceApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// dummy data
	@Bean
	CommandLineRunner run(UserService userService) {
		return args -> {
			userService.saveRole(new Role(null, "ROLE_USER"));
			userService.saveRole(new Role(null, "ROLE_MANAGER"));
			userService.saveRole(new Role(null, "ROLE_ADMIN"));
			userService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));

			userService.saveUser(new User(null, "Ieere Song", "eraser", "1234", new ArrayList<>()));
			userService.saveUser(new User(null, "Solbin Song", "solbin", "5678", new ArrayList<>()));
			userService.saveUser(new User(null, "Chanbin Song", "chanbin", "12345678", new ArrayList<>()));

			userService.addRoleToUser("eraser", "ROLE_USER");
			userService.addRoleToUser("eraser", "ROLE_MANAGER");
			userService.addRoleToUser("eraser", "ROLE_SUPER_ADMIN");

			userService.addRoleToUser("solbin", "ROLE_USER");
			userService.addRoleToUser("solbin", "ROLE_ADMIN");

			userService.addRoleToUser("chanbin", "ROLE_USER");
		};
	}

}
