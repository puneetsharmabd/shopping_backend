package com.m8.shopping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.m8.shopping.model.Account;
import com.m8.shopping.payload.apiPayload.LoginResponseDTO;
import com.m8.shopping.payload.apiPayload.SignupResponseDTO;
import com.m8.shopping.payload.auth.AccountDTO;
import com.m8.shopping.payload.auth.TokenDTO;
import com.m8.shopping.service.AccountService;
import com.m8.shopping.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
@Tag(name = "Api Controller",description = "Controller for Api Management")
@Slf4j
public class ApiController {

    String TAG = "ApiController";

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(value = "/signupStore", consumes = "application/json")
    @ApiResponse(responseCode = "200" , description = "Account added successfully.")
    @Operation(summary = "Add a new user.")
    public SignupResponseDTO signupStoreUser(@RequestBody AccountDTO accountDTO){
        log.debug(TAG, "signupStoreUser");
        try {
            if (accountDTO.getEmail() == null || accountDTO.getEmail().isEmpty() || !EMAIL_PATTERN.matcher(accountDTO.getEmail()).matches()) {
                String message = accountDTO.getEmail() == null || accountDTO.getEmail().isEmpty() 
                                ? "Email cannot be empty." 
                                : "Invalid email format.";
                return new SignupResponseDTO(
                        HttpStatus.BAD_REQUEST.value(),
                        message,
                        ""
                );
            }            
            if (accountService.existsByEmail(accountDTO)) {
                return new SignupResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Account with email " + accountDTO.getEmail() + " already exists.",
                "");
            }
            if (accountDTO.getPassword() == null || accountDTO.getPassword().isEmpty() || accountDTO.getPassword().length() < 8) {
                String message = accountDTO.getPassword() == null || accountDTO.getPassword().isEmpty()
                                ? "Password cannot be empty."
                                : "Password must be at least 8 characters long.";
                return new SignupResponseDTO(
                        HttpStatus.BAD_REQUEST.value(),
                        message,
                        ""
                );
            }            
            Account account = new Account();
            account.setEmail(accountDTO.getEmail());
            account.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
            account.setRole("ROLE_STORE");
            accountService.save(account);
            Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(accountDTO.getEmail(), accountDTO.getPassword()));
        TokenDTO tokenDTO = new TokenDTO(tokenService.generateToken(authentication));
            account.setToken(tokenDTO.getToken());
            accountService.save(account);
            return new SignupResponseDTO(HttpStatus.OK.value(), "Account created successfully.", account.getToken());
        } catch (Exception e) {
            log.debug(TAG, e.getMessage());
            return new SignupResponseDTO(HttpStatus.BAD_REQUEST.value(), "Something went wrong.", "");
        }
    }

    @PostMapping(value = "/loginStore", consumes = "application/json")
    @ApiResponse(responseCode = "200" , description = "Account added successfully.")
    @Operation(summary = "Login store user.")
    public LoginResponseDTO loginStoreUser(@RequestBody AccountDTO accountDTO){
        log.debug(TAG, "loginStoreUser");
        try {
            if (accountDTO.getEmail() == null || accountDTO.getEmail().isEmpty() || !EMAIL_PATTERN.matcher(accountDTO.getEmail()).matches()) {
                String message = accountDTO.getEmail() == null || accountDTO.getEmail().isEmpty() 
                                ? "Email cannot be empty." 
                                : "Invalid email format.";
                return new LoginResponseDTO(
                        HttpStatus.BAD_REQUEST.value(),
                        message,
                        ""
                );
            }            
            if (accountDTO.getPassword() == null || accountDTO.getPassword().isEmpty() || accountDTO.getPassword().length() < 8) {
                String message = accountDTO.getPassword() == null || accountDTO.getPassword().isEmpty()
                                ? "Password cannot be empty."
                                : "Password must be at least 8 characters long.";
                return new LoginResponseDTO(
                        HttpStatus.BAD_REQUEST.value(),
                        message,
                        ""
                );
            }   
            Optional<Account> accountOptional = accountService.findByEmail(accountDTO.getEmail());
            if (accountOptional.isEmpty()) {
                return new LoginResponseDTO(
                        HttpStatus.NOT_FOUND.value(),
                        "Account with email " + accountDTO.getEmail() + " does not exist.",
                        ""
                );
            }
            Account account = accountOptional.get();
        if (!passwordEncoder.matches(accountDTO.getPassword(), account.getPassword())) {
            return new LoginResponseDTO (
                    HttpStatus.UNAUTHORIZED.value(),
                    "Invalid credentials.",
                    ""
            );
        }
        return new LoginResponseDTO(HttpStatus.OK.value(), "Login successful.", account.getToken());
        } catch (Exception e) {
            log.debug(TAG, e.getMessage());
            return new LoginResponseDTO(HttpStatus.BAD_REQUEST.value(), "Something went wrong.", "");
        }
    }
}
