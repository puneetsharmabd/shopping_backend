package com.m8.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.m8.shopping.model.Account;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long>{
    
    Optional<Account> findByEmail(String email);
}
