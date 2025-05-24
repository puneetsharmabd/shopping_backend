package com.m8.shopping.payload.apiPayload;

import java.util.List;

import com.m8.shopping.model.photo.Photo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class PhotoDTO {
    private Long id;
    private String name;
    private String originalFileName;
    private String filename;
}
