package com.example.MusiciansAPI.controller;

import com.example.MusiciansAPI.model.APIResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequestMapping("api/protected")
public class ProtectedController {

    private String string;

    @GetMapping
    public APIResponse<String> testProtectedEndpoint() {
        var response = new APIResponse<String>();
        var string = "Success! Gained access to protected resource with JWT Token";
        response.setData(string);
        return response;
    }
}
