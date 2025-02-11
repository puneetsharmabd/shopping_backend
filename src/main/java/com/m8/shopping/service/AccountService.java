package com.m8.shopping.service;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.m8.shopping.model.Account;
import com.m8.shopping.payload.auth.AccountDTO;
import com.m8.shopping.repository.AccountRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountService implements UserDetailsService{

    String TAG = "AccountService";
    
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Account save(Account account){
        if (account.getRole() == null) {
            account.setRole("ROLE_USER");
        }
        return accountRepository.save(account);
    }

    public List<Account> findAll(){
        return accountRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findByEmail(email);
        if (!optionalAccount.isPresent()) {
            throw new UsernameNotFoundException("Account not found.");
        }

        Account account = optionalAccount.get();
        List<GrantedAuthority> grantedAuthority = new ArrayList();
        grantedAuthority.add(new SimpleGrantedAuthority(account.getRole()));
        return new User(account.getEmail(),account.getPassword(),grantedAuthority);
    }

    public boolean existsByEmail(AccountDTO accountDTO){
        if (accountRepository.existsByEmail(accountDTO.getEmail())) {
        return true;
    }
        return false;
    }

    public Optional<Account> findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    public Optional<Account> findByToken(String token) {
        log.debug(TAG, "signupStoreUser");
        return accountRepository.findByToken(token);
    }
}
