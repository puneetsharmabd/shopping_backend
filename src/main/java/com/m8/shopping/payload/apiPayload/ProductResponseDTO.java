package com.m8.shopping.payload.apiPayload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ProductResponseDTO {
    private Long id;
    private String productName;
    private String description;
    private double price;
    private int stockQuantity;
}
