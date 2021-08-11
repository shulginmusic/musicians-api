package com.example.MusiciansAPI;

import com.example.MusiciansAPI.model.Role;
import com.example.MusiciansAPI.service.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MusiciansApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusiciansApiApplication.class, args);
	}

//	@Bean
//	CommandLineRunner runner(RoleService roleService) {
//		return args -> {
//
//			roleService.create(new Role(Role.RoleName.ROLE_USER));
//			roleService.create(new Role(Role.RoleName.ROLE_USER));
//		};
//	}

}
