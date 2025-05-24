package com.m8.shopping.payload.apiPayload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class GetProductResponseDTO {
    private Long id;
    private String productName;
    private String description;
    private double price;
    private int stockQuantity;
    private List<String> photoUrls;
}
