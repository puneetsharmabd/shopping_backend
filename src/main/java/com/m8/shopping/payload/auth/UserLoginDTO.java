package com.m8.shopping.payload.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDTO{
    
    @Email
    @Schema(description = "Email address" , example = "admin@gmail.com" , requiredMode = RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "Password" , example = "password@8" , requiredMode = RequiredMode.REQUIRED)
    private String password;
}
