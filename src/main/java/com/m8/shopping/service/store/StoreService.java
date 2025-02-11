package com.m8.shopping.service.store;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.m8.shopping.model.store.Store;
import com.m8.shopping.repository.store.StoreRepository;

@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    // Create a new store
    public Store createStore(Store store) {
        return storeRepository.save(store);
    }

    public Optional<Store> getStoreById(Long storeId) {
        return storeRepository.findById(storeId);
    }

    public void updateStore(Store store) {
        storeRepository.save(store);
    }

    public void deleteStore(Long storeId) {
        storeRepository.deleteById(storeId);
    }

    // Method to get all stores
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

}
