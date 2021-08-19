package com.example.MusiciansAPI.controller;

import com.example.MusiciansAPI.model.APIResponse;
import com.example.MusiciansAPI.model.User;
import com.example.MusiciansAPI.payload.request.LoginRequest;
import com.example.MusiciansAPI.payload.request.RegistrationRequest;
import com.example.MusiciansAPI.payload.request.TokenRefreshRequest;
import com.example.MusiciansAPI.payload.request.response.JwtAuthenticationResponse;
import com.example.MusiciansAPI.repository.RoleRepository;
import com.example.MusiciansAPI.repository.UserRepository;
import com.example.MusiciansAPI.security.JwtTokenProvider;
import com.example.MusiciansAPI.service.RefreshTokenService;
import com.example.MusiciansAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
@RequestMapping("api/auth")
public class AuthenticationController {

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public APIResponse<User> register(@Valid @RequestBody RegistrationRequest registrationRequest) {
        var apiResponse = new APIResponse<User>();

        try {
            userService.registerUser(registrationRequest);
            apiResponse.setData(userService.getUserInResponse(registrationRequest));
        } catch (Exception exc) {
            apiResponse.setError(exc.getMessage());
        }

        return apiResponse;
    }

    @PostMapping("/login")
    public APIResponse<?> loginUserAuthentication(@Valid @RequestBody LoginRequest loginRequest) {
        var apiResponse = new APIResponse<String>();
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
        String jwt = tokenProvider.generateToken(authentication);

        apiResponse.setData(jwt);
        return apiResponse;
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

//    @PostMapping("/logout")
//    public ResponseEntity<?> logoutUser(@Valid @RequestBody LogOutRequest logOutRequest) {
//        refreshTokenService.deleteByUserId(logOutRequest.getUserId());
//        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
//    }

}




















