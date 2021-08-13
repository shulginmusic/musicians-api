package com.example.MusiciansAPI.controller;

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

/**
 * Login + Registration Controller
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public APIResponse<User> register (@Valid @RequestBody RegistrationRequest registrationRequest) {
        var apiResponse = new APIResponse<User>();

        try {
            userService.registerUser(registrationRequest);
            apiResponse.setData(userService.getUserInResponse(registrationRequest));
        } catch (Exception exc) {
            apiResponse.setError(exc.getMessage());
        }
        
        return apiResponse;
    }

}




















