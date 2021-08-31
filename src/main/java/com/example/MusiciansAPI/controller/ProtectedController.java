package com.example.MusiciansAPI.controller;

import com.example.MusiciansAPI.payload.request.response.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("api/protected")
public class ProtectedController {

    private String string;

    @GetMapping
    public APIResponse<String> testProtectedEndpoint() {
        var response = new APIResponse<String>();
        var string = "Success! Gained access to protected resource with JWT Token";
        try {
            response.setData(string);
        } catch (Exception exc) {
            response.setError(exc.getMessage());
        }
        return response;
    }

//    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
//    @ExceptionHandler
//    public void unauthorizedHandler() throws Exception {
//        throw new Exception("Token invalid or expired");
//    }
}
