package com.example.MusiciansAPI.payload.request.response;

import com.example.MusiciansAPI.model.User;
import lombok.Data;

@Data
public class JwtAuthenticationResponse {

    private String refreshToken;
    private User user;
    private String accessToken;
}
