package com.m8.shopping.payload.apiPayload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDTO {
    
    int status;

    String message;

    String token;
}
