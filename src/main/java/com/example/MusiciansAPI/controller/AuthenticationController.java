package com.example.MusiciansAPI.controller;

import com.example.MusiciansAPI.exception.TokenRefreshException;
import com.example.MusiciansAPI.model.APIResponse;
import com.example.MusiciansAPI.model.RefreshToken;
import com.example.MusiciansAPI.model.User;
import com.example.MusiciansAPI.payload.request.LoginRequest;
import com.example.MusiciansAPI.payload.request.RegistrationRequest;
import com.example.MusiciansAPI.payload.request.TokenRefreshRequest;
import com.example.MusiciansAPI.payload.request.response.JwtAuthenticationResponse;
import com.example.MusiciansAPI.security.JwtTokenProvider;
import com.example.MusiciansAPI.security.service.RefreshTokenService;
import com.example.MusiciansAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.validation.Valid;
import javax.validation.constraints.NotBlank;


/**
 * Login + Registration Controller
 */
@RestController
@RequestMapping("api/auth")
public class AuthenticationController {

    @Autowired
    UserService userService;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    /**
     * Registration method
     *
     * @param registrationRequest
     * @return API response with user with a decoded password for demonstration purposes
     */
    @PostMapping("/register")
    public APIResponse<User> register(@Valid @RequestBody RegistrationRequest registrationRequest) {
        var apiResponse = new APIResponse<User>();
        try {
            apiResponse.setData(userService.registerUser(registrationRequest));
        } catch (Exception exc) {
            apiResponse.setError(exc.getMessage());
        }
        return apiResponse;
    }

    @PostMapping("/login")
    public APIResponse<JwtAuthenticationResponse> loginUserAuthentication(@Valid @RequestBody LoginRequest loginRequest) {
        var apiResponse = new APIResponse<JwtAuthenticationResponse>();
        try {
            apiResponse.setData(userService.authenticateUser(loginRequest));
        } catch (Exception exc) {
            apiResponse.setError(exc.getMessage());
        }
        return apiResponse;
    }

    @PostMapping("/refresh-access")
    public APIResponse<String> refreshAccess(@Valid @RequestBody TokenRefreshRequest request) {
        var apiResponse = new APIResponse<String>();
        try {
            apiResponse.setData(refreshTokenService.refreshAccessToken(request));
        } catch (Exception exc) {
            apiResponse.setError(exc.getMessage());
        }
        return apiResponse;
    }

//    @PostMapping("/logout")
//    public ResponseEntity<?> logoutUser(@Valid @RequestBody LogOutRequest logOutRequest) {
//        refreshTokenService.deleteByUserId(logOutRequest.getUserId());
//        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
//    }
}




















