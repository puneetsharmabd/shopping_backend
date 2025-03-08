package com.m8.shopping.controller.store;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.m8.shopping.model.Account;
import com.m8.shopping.model.store.Store;
import com.m8.shopping.payload.apiPayload.GenericResponseDTO;
import com.m8.shopping.payload.apiPayload.StoreDTO;
import com.m8.shopping.payload.apiPayload.StoreResponseDTO;
import com.m8.shopping.service.AccountService;
import com.m8.shopping.service.store.StoreService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/stores")
@Tag(name = "Store Controller", description = "Controller for Store Management")
@Slf4j
public class StoreController {
    String TAG = "StoreController";

    @Autowired
    private StoreService storeService;

    @Autowired
    private AccountService accountService;

    @PostMapping("/create")
    @SecurityRequirement(name = "shopping-api")
    public GenericResponseDTO<StoreResponseDTO> createStore(@RequestBody StoreDTO storeDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new GenericResponseDTO<>(HttpStatus.UNAUTHORIZED.value(), "Unauthorized access", null);
        }
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String email = jwt.getClaim("sub");
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        if (optionalAccount.isEmpty()) {
            return new GenericResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Invalid account token", null);
        }

        Account account = optionalAccount.get();

        // Logic to save store and associate it with the account
        Store store = new Store();
        store.setName(storeDTO.getName());
        store.setAddress(storeDTO.getAddress());
        store.setPhone(storeDTO.getPhone());
        storeService.createStore(store);
        account.setStore(store);

        accountService.save(account); // Save account with the store

        StoreResponseDTO storeResponseDTO = new StoreResponseDTO(store.getId(), store.getName(), store.getAddress(),
                store.getPhone());

        return new GenericResponseDTO<>(HttpStatus.OK.value(), "Store created successfully", storeResponseDTO);
    }

    @GetMapping("/getStoreDetails")
    public GenericResponseDTO<StoreResponseDTO> getStoreDetails(@RequestParam Long storeId) {
        // Check if the store exists
        Optional<Store> optionalStore = storeService.getStoreById(storeId);
        if (optionalStore.isEmpty()) {
            return new GenericResponseDTO<>(HttpStatus.NOT_FOUND.value(), "Store not found", null);
        }

        Store store = optionalStore.get();

        StoreResponseDTO storeResponseDTO = new StoreResponseDTO(store.getId(), store.getName(), store.getAddress(),
                store.getPhone());
        return new GenericResponseDTO<>(HttpStatus.OK.value(), "Store retrieved successfully", storeResponseDTO);
    }

    @PostMapping("/update")
    @SecurityRequirement(name = "shopping-api")
    public GenericResponseDTO<StoreResponseDTO> updateStore(@RequestParam Long storeId,
            @RequestBody StoreDTO storeDTO) {
        // Authenticate user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new GenericResponseDTO<>(HttpStatus.UNAUTHORIZED.value(), "Unauthorized access", null);
        }

        Jwt jwt = (Jwt) authentication.getPrincipal();
        String email = jwt.getClaim("sub");
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        if (optionalAccount.isEmpty()) {
            return new GenericResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Invalid account token", null);
        }

        Account account = optionalAccount.get();

        // Check if the store exists
        Optional<Store> optionalStore = storeService.getStoreById(storeId);
        if (optionalStore.isEmpty()) {
            return new GenericResponseDTO<>(HttpStatus.NOT_FOUND.value(), "Store not found", null);
        }

        Store store = optionalStore.get();

        // Validate that the store belongs to the logged-in account
        if (!store.equals(account.getStore())) {
            return new GenericResponseDTO<>(HttpStatus.FORBIDDEN.value(), "You are not authorized to update this store",
                    null);
        }

        // Update the store
        store.setName(storeDTO.getName());
        store.setAddress(storeDTO.getAddress());
        store.setPhone(storeDTO.getPhone());
        storeService.updateStore(store);

        StoreResponseDTO storeResponseDTO = new StoreResponseDTO(store.getId(), store.getName(), store.getAddress(),
                store.getPhone());
        return new GenericResponseDTO<>(HttpStatus.OK.value(), "Store updated successfully", storeResponseDTO);
    }

    @PostMapping("/delete")
    @SecurityRequirement(name = "shopping-api")
    public GenericResponseDTO<String> deleteStore(@RequestParam Long storeId) {
        // Authenticate user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new GenericResponseDTO<>(HttpStatus.UNAUTHORIZED.value(), "Unauthorized access", null);
        }

        Jwt jwt = (Jwt) authentication.getPrincipal();
        String email = jwt.getClaim("sub");
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        if (optionalAccount.isEmpty()) {
            return new GenericResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Invalid account token", null);
        }

        Account account = optionalAccount.get();

        // Check if the store exists
        Optional<Store> optionalStore = storeService.getStoreById(storeId);
        if (optionalStore.isEmpty()) {
            return new GenericResponseDTO<>(HttpStatus.NOT_FOUND.value(), "Store not found", null);
        }

        Store store = optionalStore.get();

        // Validate that the store belongs to the logged-in account
        if (!store.equals(account.getStore())) {
            return new GenericResponseDTO<>(HttpStatus.FORBIDDEN.value(), "You are not authorized to delete this store",
                    null);
        }

        // Delete the store
        storeService.deleteStore(storeId);

        return new GenericResponseDTO<>(HttpStatus.OK.value(), "Store deleted successfully",
                "Store with ID " + storeId + " has been deleted.");
    }

    @GetMapping("/getAllStores")
    @SecurityRequirement(name = "shopping-api")
    public GenericResponseDTO<List<StoreResponseDTO>> getAllStores() {
        // Retrieve all stores
        List<Store> stores = storeService.getAllStores();
        if (stores.isEmpty()) {
            return new GenericResponseDTO<>(HttpStatus.NOT_FOUND.value(), "No stores found", null);
        }

        // Convert Store entities to StoreResponseDTOs
        List<StoreResponseDTO> storeResponseDTOs = stores.stream()
                .map(store -> new StoreResponseDTO(store.getId(), store.getName(), store.getAddress(),
                        store.getPhone()))
                .collect(Collectors.toList());

        return new GenericResponseDTO<>(HttpStatus.OK.value(), "Stores retrieved successfully", storeResponseDTOs);
    }

}
