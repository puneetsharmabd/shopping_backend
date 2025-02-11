package com.m8.shopping.repository.store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.m8.shopping.model.store.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    
}
