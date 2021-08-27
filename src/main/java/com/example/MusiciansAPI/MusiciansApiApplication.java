package com.example.MusiciansAPI;

import com.example.MusiciansAPI.model.Role;
import com.example.MusiciansAPI.repository.RoleRepository;
import com.example.MusiciansAPI.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class MusiciansApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusiciansApiApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(RoleRepository roleRepository) {
		return args -> {
			//Delete roles from repo if there are any left from previous run (Thank you Ryan Desmond)
			roleRepository.deleteAll();
			//Save roles at runtime
			Role userRole = new Role(Role.RoleName.ROLE_USER);
			Role adminRole = new Role(Role.RoleName.ROLE_ADMIN);

			roleRepository.save(userRole);
			roleRepository.save(adminRole);
		};
	}

}
