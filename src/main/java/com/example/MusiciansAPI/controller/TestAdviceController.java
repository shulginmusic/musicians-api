package com.example.MusiciansAPI.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestAdviceController {
    @GetMapping("/test-advice")
    public void testAdvice() throws Exception {
        throw new Exception("Advice tested");
    }
}
