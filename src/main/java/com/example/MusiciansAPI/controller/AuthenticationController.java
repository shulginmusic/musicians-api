package com.example.MusiciansAPI.controller;

//Controller for login / registration

import com.example.MusiciansAPI.model.APIResponse;
import com.example.MusiciansAPI.model.User;
import com.example.MusiciansAPI.payload.request.RegistrationRequest;
import com.example.MusiciansAPI.repository.RoleRepository;
import com.example.MusiciansAPI.repository.UserRepository;
import com.example.MusiciansAPI.security.JwtTokenProvider;
import com.example.MusiciansAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public APIResponse<User> register (@Valid @RequestBody RegistrationRequest registrationRequest) {
        //TODO:Refactor to service

        var apiResponse = new APIResponse<User>();

        try {
            userService.registerUser(registrationRequest);
            apiResponse.setData(userService.getUserByUsername(registrationRequest.getUsername()));
        } catch (Exception exc) {
            apiResponse.setError(exc.getMessage());
        }

//        //TODO what is this?
//        URI location = ServletUriComponentsBuilder
//                .fromCurrentContextPath().path("/api/users/{username}")
//                .buildAndExpand(result.getUsername()).toUri();
        return apiResponse;

//        //TODO:Refactor to service
//
//        //Username taken
//        if (userRepository.existsByUsername(RegistrationRequest.getUsername())) {
//            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
//                    HttpStatus.BAD_REQUEST);
//        }
//
//        //Email taken
//        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
//            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
//                    HttpStatus.BAD_REQUEST);
//        }
//
//        //Create new user
//        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
//                signUpRequest.getEmail(), signUpRequest.getPassword());
//
//        //Encode password
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//
//        //Set user role
//        Role userRole = roleRepository.findByName(Role.RoleName.ROLE_USER)
//                .orElseThrow(() -> new AppException("No user role set"));
//        user.setRoles(Collections.singleton(userRole));
//
//        User result = userRepository.save(user);
//
//        //TODO what is this?
//        URI location = ServletUriComponentsBuilder
//                .fromCurrentContextPath().path("/api/users/{username}")
//                .buildAndExpand(result.getUsername()).toUri();
//
//        return ResponseEntity.created(location).body(new ApiResponse(true, "User Registration OK"));

    }
}




















