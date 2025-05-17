package com.m8.shopping.controller.product;

import com.m8.shopping.model.Account;
import com.m8.shopping.model.photo.Photo;
import com.m8.shopping.model.product.Product;
import com.m8.shopping.model.store.Store;
import com.m8.shopping.payload.apiPayload.GenericResponseDTO;
import com.m8.shopping.payload.apiPayload.ProductDTO;
import com.m8.shopping.payload.apiPayload.ProductResponseDTO;
import com.m8.shopping.payload.apiPayload.PhotoResponseDTO;
import com.m8.shopping.service.AccountService;
import com.m8.shopping.service.photo.PhotoService;
import com.m8.shopping.service.product.ProductService;
import com.m8.shopping.service.store.StoreService;
import com.m8.shopping.util.utils.AppUtil;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.http.MediaType;
import java.util.Arrays;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/products")
@Tag(name = "V1 Product Controller", description = "Controller for Product Management")
@Slf4j
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PhotoService photoService;

    @PostMapping(value = "/create")
    @SecurityRequirement(name = "shopping-api")
    public GenericResponseDTO<ProductResponseDTO> createProduct(@RequestBody ProductDTO productDTO) {

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

        Optional<Store> optionalStore = storeService.getStoreById(productDTO.getStoreId());
        if (optionalStore.isEmpty()) {
            return new GenericResponseDTO<>(HttpStatus.NOT_FOUND.value(), "Store not found", null);
        }

        Store store = optionalStore.get();
        if (!store.equals(account.getStore())) {
            return new GenericResponseDTO<>(HttpStatus.FORBIDDEN.value(),
                    "You are not authorized to add products to this store", null);
        }

        Product product = new Product();
        product.setProductName(productDTO.getProductName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStockQuantity(productDTO.getStockQuantity());
        product.setStore(store);
        productService.createProduct(product);

        ProductResponseDTO productResponseDTO = new ProductResponseDTO(
                product.getId(),
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity());

        return new GenericResponseDTO<>(HttpStatus.OK.value(), "Product created successfully", productResponseDTO);
    }

    @GetMapping("/getProductDetails")
    public GenericResponseDTO<ProductResponseDTO> getProductDetails(@RequestParam Long productId) {
        Optional<Product> optionalProduct = productService.getProductById(productId);
        if (optionalProduct.isEmpty()) {
            return new GenericResponseDTO<>(HttpStatus.NOT_FOUND.value(), "Product not found", null);
        }

        Product product = optionalProduct.get();

        ProductResponseDTO productResponseDTO = new ProductResponseDTO(
                product.getId(),
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity());

        return new GenericResponseDTO<>(HttpStatus.OK.value(), "Product retrieved successfully", productResponseDTO);
    }

    @PostMapping("/update")
    @SecurityRequirement(name = "shopping-api")
    public GenericResponseDTO<ProductResponseDTO> updateProduct(
            @RequestParam Long productId,
            @RequestBody ProductDTO productDTO) {
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

        Optional<Product> optionalProduct = productService.getProductById(productId);
        if (optionalProduct.isEmpty()) {
            return new GenericResponseDTO<>(HttpStatus.NOT_FOUND.value(), "Product not found", null);
        }

        Product product = optionalProduct.get();

        if (!product.getStore().equals(account.getStore())) {
            return new GenericResponseDTO<>(HttpStatus.FORBIDDEN.value(),
                    "You are not authorized to update this product", null);
        }

        product.setProductName(productDTO.getProductName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStockQuantity(productDTO.getStockQuantity());

        productService.updateProduct(product);

        ProductResponseDTO productResponseDTO = new ProductResponseDTO(
                product.getId(),
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity());

        return new GenericResponseDTO<>(HttpStatus.OK.value(), "Product updated successfully", productResponseDTO);
    }

    @PostMapping("/delete")
    @SecurityRequirement(name = "shopping-api")
    public GenericResponseDTO<String> deleteProduct(@RequestParam Long productId) {
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

        Optional<Product> optionalProduct = productService.getProductById(productId);
        if (optionalProduct.isEmpty()) {
            return new GenericResponseDTO<>(HttpStatus.NOT_FOUND.value(), "Product not found", null);
        }

        Product product = optionalProduct.get();

        if (!product.getStore().equals(account.getStore())) {
            return new GenericResponseDTO<>(HttpStatus.FORBIDDEN.value(),
                    "You are not authorized to delete this product", null);
        }

        productService.deleteProduct(productId);
        return new GenericResponseDTO<>(HttpStatus.OK.value(), "Product deleted successfully",
                "Product with ID " + productId + " has been deleted.");
    }

    @GetMapping("/getAllProducts")
    @SecurityRequirement(name = "shopping-api")
    public GenericResponseDTO<List<ProductResponseDTO>> getAllProducts() {
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
        List<Product> products = productService.getAllProducts();

        if (products.isEmpty()) {
            return new GenericResponseDTO<>(HttpStatus.NOT_FOUND.value(), "No products found", null);
        }

        // Filter products by store owned by the account.
        List<ProductResponseDTO> productResponseDTOs = products.stream()
                .filter(product -> product.getStore().equals(account.getStore()))
                .map(product -> new ProductResponseDTO(
                        product.getId(),
                        product.getProductName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getStockQuantity()))
                .collect(Collectors.toList());

        return new GenericResponseDTO<>(HttpStatus.OK.value(), "Products retrieved successfully", productResponseDTOs);
    }

    @PostMapping(value = "/photo/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "shopping-api")
    public GenericResponseDTO<PhotoResponseDTO> createPhoto(
            @RequestParam("productId") Long productId,
            @RequestPart(required = true) MultipartFile[] files) throws IOException {

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

        Optional<Product> optionalProduct = productService.getProductById(productId);
        if (optionalProduct.isEmpty()) {
            return new GenericResponseDTO<>(HttpStatus.NOT_FOUND.value(), "Product not found", null);
        }

        Product product = optionalProduct.get();
        // Check if the user is authorized to add photos to this product's store.
        if (!product.getStore().equals(account.getStore())) {
            return new GenericResponseDTO<>(HttpStatus.FORBIDDEN.value(),
                    "You are not authorized to add photos to this product", null);
        }

        List<String> fileNamesWithSuccess = new ArrayList<>();
        List<String> fileNamesWithError = new ArrayList<>();
        Photo photo = new Photo();

        Arrays.asList(files).stream().forEach(file -> {
            String contentType = file.getContentType();
            if (contentType != null) {
                if (contentType.equals("image/png")
                        || contentType.equals("image/jpg")
                        || contentType.equals("image/jpeg")) {
                    fileNamesWithSuccess.add(file.getOriginalFilename());

                    int length = 10;
                    boolean useLetters = true;
                    boolean useNumbers = true;

                    try {
                        String fileName = file.getOriginalFilename();
                        String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
                        String final_photo_name = generatedString + fileName;
                        String absolute_fileLocation = AppUtil.get_photo_upload_path(final_photo_name, productId);
                        Path path = Paths.get(absolute_fileLocation);
                        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                        photo.setName(fileName);
                        photo.setFilename(final_photo_name);
                        photo.setOriginalFileName(fileName);
                        photo.setProduct(product);
                        photoService.createPhoto(photo);

                    } catch (Exception e) {
                        Logger.getLogger(e.getMessage());
                        fileNamesWithError.add(file.getOriginalFilename());
                    }

                } else {
                    fileNamesWithError.add(file.getOriginalFilename());
                }
            } else {

            }
        });
        PhotoResponseDTO photoResponseDTO = new PhotoResponseDTO(photo.getId(),photo.getName(),photo.getOriginalFileName(),photo.getFilename(),photo.getProduct().getId());

        return new GenericResponseDTO<>(HttpStatus.OK.value(), "Photo created successfully", photoResponseDTO);
    }

}
