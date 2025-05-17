package com.m8.shopping.payload.apiPayload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class PhotoResponseDTO {
    private Long id;
    private String name;
    private String originalFileName;
    private String filename;
    private Long productId;
}
