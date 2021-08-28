package com.example.MusiciansAPI.controller;

import com.example.MusiciansAPI.payload.request.response.APIResponse;
import com.example.MusiciansAPI.model.User;
import com.example.MusiciansAPI.payload.request.LoginRequest;
import com.example.MusiciansAPI.payload.request.RegistrationRequest;
import com.example.MusiciansAPI.payload.request.TokenRefreshRequest;
import com.example.MusiciansAPI.payload.request.response.JwtAuthenticationResponse;
import com.example.MusiciansAPI.security.JwtTokenProvider;
import com.example.MusiciansAPI.security.service.RefreshTokenService;
import com.example.MusiciansAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


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

    /**
     * Login method
     * @return API response with JwtAuthenticationResponse that includes the refresh, access tokens and a user "in response"
     */
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

    /**
     * Refresh access token method
     * @param request provide the refresh token (token should be valid and non-expired)
     * @return new access token
     */
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

    @PostMapping("/logout/{id}")
    public APIResponse<String> logoutUser(@PathVariable long id) {
        var apiResponse = new APIResponse<String>();
        try {
            refreshTokenService.deleteByUserId(id);
            apiResponse.setData("Logout successful for user with id " + id);
        } catch (Exception exc) {
            apiResponse.setError(exc.getMessage());
        }
        return apiResponse;
    }

    @GetMapping("/test-advice")
    public void testAdvice() throws Exception {
        throw new Exception("Advice tested");
    }
}




















