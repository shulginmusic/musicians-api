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
 *
 * Ref: https://github.com/bezkoder/spring-boot-refresh-token-jwt
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

    @Autowired
    JwtTokenProvider jwtTokenProvider;
    private @NotBlank String refreshToken;

    /**
     * Registration method
     *
     * @param registrationRequest
     * @return API response with user with a decoded password for demonstration purposes
     */
    @PostMapping("/register")
    public APIResponse<User> register(@Valid @RequestBody RegistrationRequest registrationRequest) {
        var apiResponse = new APIResponse<User>();
//        var jwtResponse = new JwtAuthenticationResponse();
        try {
            userService.registerUser(registrationRequest);
            var userInResponse = userService.getUserInResponse(registrationRequest);
//            var refreshToken = refreshTokenService.findByUser(userInResponse);
//            jwtResponse.setRefreshToken(refreshToken.get().getToken());
//            jwtResponse.setUser(userInResponse);
            apiResponse.setData(userInResponse);
        } catch (Exception exc) {
            apiResponse.setError(exc.getMessage());
        }

        return apiResponse;
    }

    @PostMapping("/login")
    public APIResponse<JwtAuthenticationResponse> loginUserAuthentication(@Valid @RequestBody LoginRequest loginRequest) {
        var apiResponse = new APIResponse<JwtAuthenticationResponse>();
        var jwtResponse = new JwtAuthenticationResponse();

        //Auth object
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        //Set auth
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //Create new refresh token
        var user = userService.getUserByUsernameOrEmail(loginRequest.getUsernameOrEmail());
        refreshTokenService.createRefreshToken(user.getId());

        //Show token in response
        var refreshToken = refreshTokenService.findByUser(user);
        jwtResponse.setRefreshToken(refreshToken.get().getToken());
        jwtResponse.setUser(user);

        //Generate jwt
        String jwtToken = tokenProvider.generateToken(authentication);
        jwtResponse.setAccessToken(jwtToken);

        apiResponse.setData(jwtResponse);
        return apiResponse;
    }

    @PostMapping("/refreshtoken")
    public APIResponse<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        var apiResponse = new APIResponse<String>();

        refreshToken = request.getRefreshToken();
        return refreshTokenService.findByToken(refreshToken)
                //verify expiration
                .map(refreshTokenService::verifyExpiration)
                //verify token is assigned to a user
                .map(RefreshToken::getUser)
                //Generate new access token
                .map(user -> {
                    String newAccessToken = jwtTokenProvider.generateToken(user.getId());
                    apiResponse.setData(newAccessToken);
                    return apiResponse;
                })
                .orElseThrow(() -> new TokenRefreshException(refreshToken,
                        "Refresh token is not in database!"));

    }

//    @PostMapping("/logout")
//    public ResponseEntity<?> logoutUser(@Valid @RequestBody LogOutRequest logOutRequest) {
//        refreshTokenService.deleteByUserId(logOutRequest.getUserId());
//        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
//    }

}




















