package com.example.MusiciansAPI.service;

import com.example.MusiciansAPI.exception.AppException;
import com.example.MusiciansAPI.model.Role;
import com.example.MusiciansAPI.model.User;
import com.example.MusiciansAPI.payload.request.RegistrationRequest;
import com.example.MusiciansAPI.repository.RoleRepository;
import com.example.MusiciansAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleService roleService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void registerUser(RegistrationRequest registrationRequest) throws Exception {
        //Check if username / email is already taken
        checkIfUsernameTaken(registrationRequest.getUsername());
        checkIfEmailTaken(registrationRequest.getEmail());

        //New user
        var user = new User(registrationRequest.getName(), registrationRequest.getUsername(),
                registrationRequest.getEmail(), registrationRequest.getPassword());

        //Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //Set user role
//        Optional<Role> userRole = roleRepository.findByName(Role.RoleName.ROLE_USER);
//        user.setRoles(Collections.singleton(userRole.get()));

        Role userRole = roleRepository.findByName(Role.RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("No user role set"));
        user.setRoles(Collections.singleton(userRole));

        userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(user.getUsername()).toUri();

    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).get();
    }

    public void checkIfUsernameTaken(String username) throws Exception {
        if (userRepository.existsByUsername(username)){
            throw new Exception("Username " + username + " already taken!");
        }
    }

    public void checkIfEmailTaken(String email) throws Exception {
        if (userRepository.existsByEmail(email)) {
            throw new Exception("Email " + email + " already taken!");
        }
    }
}
