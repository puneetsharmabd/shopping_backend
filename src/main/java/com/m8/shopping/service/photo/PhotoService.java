package com.m8.shopping.service.photo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.m8.shopping.model.photo.Photo;
import com.m8.shopping.repository.photo.PhotoRepository;
import java.util.List;
import java.util.Optional;

@Service
public class PhotoService {
    @Autowired
    private PhotoRepository photoRepository;

    public Photo createPhoto(Photo photo) {
        return photoRepository.save(photo);
    }

    public Optional<Photo> getPhotoById(Long photoId) {
        return photoRepository.findById(photoId);
    }

    public Photo updatePhoto(Photo photo) {
        return photoRepository.save(photo);
    }

    public void deletePhoto(Long photoId) {
        photoRepository.deleteById(photoId);
    }

    public List<Photo> getAllPhotos() {
        return photoRepository.findAll();
    }

    public List<Photo> getPhotosByProductId(Long productId){
        return photoRepository.findByProduct_Id(productId);
    }
}
