package com.m8.shopping.model.photo;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import com.m8.shopping.model.product.Product;

@Entity
@Getter
@Setter
@ToString
public class Photo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String originalFileName;
    private String filename;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
