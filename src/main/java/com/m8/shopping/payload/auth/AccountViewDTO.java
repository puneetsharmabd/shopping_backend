package com.m8.shopping.payload.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AccountViewDTO {

    private String email;

    private String role;
}
