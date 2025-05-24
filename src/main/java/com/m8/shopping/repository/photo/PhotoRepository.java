package com.m8.shopping.repository.photo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.m8.shopping.model.photo.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByProduct_Id(Long productId);
}
