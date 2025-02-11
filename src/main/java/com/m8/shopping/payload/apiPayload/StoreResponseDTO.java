package com.m8.shopping.payload.apiPayload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class StoreResponseDTO {
    private Long id;
    
    private String name;

    private String address;

    private String phone;
}
