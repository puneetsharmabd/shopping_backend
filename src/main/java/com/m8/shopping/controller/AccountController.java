package com.m8.shopping.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
public class AccountController {
    
    @GetMapping("/")
    public String demo(){
        return "Hello World";
    }

    @GetMapping("/test")
    @Tag(name = "Test",description = "This is a test api")
    @SecurityRequirement(name = "shopping-api")
    public String test(){
        return "Demo test";
    }
}
