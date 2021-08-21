package com.example.MusiciansAPI.service;

import com.example.MusiciansAPI.exception.AppException;
import com.example.MusiciansAPI.model.Role;
import com.example.MusiciansAPI.model.User;
import com.example.MusiciansAPI.payload.request.LoginRequest;
import com.example.MusiciansAPI.payload.request.RegistrationRequest;
import com.example.MusiciansAPI.payload.request.response.JwtAuthenticationResponse;
import com.example.MusiciansAPI.repository.RoleRepository;
import com.example.MusiciansAPI.repository.UserRepository;
import com.example.MusiciansAPI.security.JwtTokenProvider;
import com.example.MusiciansAPI.security.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    public void registerUser(RegistrationRequest registrationRequest) throws Exception {
        //Check if username / email is already taken
        checkIfUsernameTaken(registrationRequest.getUsername());
        checkIfEmailTaken(registrationRequest.getEmail());

        //New user
        var user = new User(registrationRequest.getName(), registrationRequest.getUsername(),
                registrationRequest.getEmail(), registrationRequest.getPassword());

        //Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(Role.RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("No user role set"));
        user.setRoles(Collections.singleton(userRole));

        userRepository.save(user);

    }

    public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest) throws Exception {
        var jwtResponse = new JwtAuthenticationResponse();
        var user = getUserByUsernameOrEmail(loginRequest.getUsernameOrEmail());

        //Auth object
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );
        //Set auth
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //Generate jwt
        String jwt = jwtTokenProvider.generateToken(authentication);

        //Just delete the old and create a new refresh token
        // (alternatively you could check the refresh token's expiration and
        // keep it the same, but I personally believe that with each new login comes a new refresh token)
        if (refreshTokenService.findByUser(user).isPresent()) {
            refreshTokenService.deleteByUserId(user.getId());
        }
        //Create brand new refresh token
        refreshTokenService.createRefreshToken(user.getId());

        var refreshToken = refreshTokenService.findByUser(user);
        jwtResponse.setRefreshToken(refreshToken.get().getToken());
        jwtResponse.setUser(getUserInResponse(loginRequest));

        //Generate jwt
        String jwtToken = jwtTokenProvider.generateToken(authentication);
        jwtResponse.setAccessToken(jwtToken);

        return jwtResponse;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).get();
    }
    public User getUserByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).get();
    }

    /**
     * This method is used for returning a user with a decoded password
     * @param registrationRequest
     * @return User with decoded password (the password stays encoded in the database)
     * @throws Exception if password and the encoded password don't match
     */
    public User getUserInResponse(RegistrationRequest registrationRequest) throws Exception{
        //Get the user to return in response
        var userInResponse = getUserByUsername(registrationRequest.getUsername());

        //Obtain the decoded password
        var decodedPassword = registrationRequest.getPassword();

        //Make sure the password matches with the BCrypt Encoded password
        if (passwordEncoder.matches(decodedPassword, userInResponse.getPassword())) {
            //Set the decoded password to display in response
            userInResponse.setPassword(decodedPassword);
        } else {
            throw new Exception("Password encoding failed.");
        }
        return userInResponse;
    }

    //Another method signature for login
    public User getUserInResponse(LoginRequest loginRequest) throws Exception{

        //Get the user to return in response
        var userInResponse = getUserByUsernameOrEmail(loginRequest.getUsernameOrEmail());

        //Obtain the decoded password
        var decodedPassword = loginRequest.getPassword();

        //Make sure the password matches with the BCrypt Encoded password
        if (passwordEncoder.matches(decodedPassword, userInResponse.getPassword())) {
            //Set the decoded password to display in response
            userInResponse.setPassword(decodedPassword);
        } else {
            throw new Exception("Password encoding failed.");
        }
        return userInResponse;
    }

    //Helper Methods

    private void checkIfUsernameTaken(String username) throws Exception {
        if (userRepository.existsByUsername(username)){
            throw new Exception("Username " + username + " already taken!");
        }
    }

    private void checkIfEmailTaken(String email) throws Exception {
        if (userRepository.existsByEmail(email)) {
            throw new Exception("Email " + email + " already taken!");
        }
    }
}
