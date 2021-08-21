package com.example.MusiciansAPI.payload.request.response;

import com.example.MusiciansAPI.model.User;
import lombok.Data;

/**
 * This class is used to display data upon successful login
 * It provides the refresh token, the current access token and the user info
 */
@Data
public class JwtAuthenticationResponse {

    private String refreshToken;
    private User user;
    private String accessToken;
}
