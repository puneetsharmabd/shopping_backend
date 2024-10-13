package com.m8.shopping.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.m8.shopping.model.Account;
import com.m8.shopping.service.AccountService;

@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    private AccountService accountService;

    @Override
    public void run(String... args) throws Exception {
        Account account1 = new Account();
        account1.setEmail("admin@gmail.com");
        account1.setPassword("admin");
        account1.setRole("ROLE_ADMIN");
        accountService.save(account1);

        Account account2 = new Account();
        account2.setEmail("puneet@gmail.com");
        account2.setPassword("puneet");
        account2.setRole("ROLE_USER");
        accountService.save(account2);
    }
    
}
