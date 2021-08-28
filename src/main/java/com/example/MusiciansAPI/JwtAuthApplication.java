package com.example.MusiciansAPI;

import com.example.MusiciansAPI.model.Role;
import com.example.MusiciansAPI.repository.RoleRepository;
import com.example.MusiciansAPI.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JwtAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtAuthApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(RoleRepository roleRepository, UserRepository userRepository) {
		return args -> {
			//Leave this permanently
			if (roleRepository.findAll().size() == 0) {
				//Save roles at runtime
				Role userRole = new Role(Role.RoleName.ROLE_USER);
				Role adminRole = new Role(Role.RoleName.ROLE_ADMIN);

				roleRepository.save(userRole);
				roleRepository.save(adminRole);
			}
		};
	}

}
