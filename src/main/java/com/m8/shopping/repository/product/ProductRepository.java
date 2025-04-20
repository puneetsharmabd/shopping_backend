package com.m8.shopping.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.m8.shopping.model.product.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
    
}
