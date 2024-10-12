package com.m8.shopping.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Server API",
        version = "Version 1.0",
        contact = @Contact(
            name = "M8", email = "puneetsharmabd@gmail.com", url = "https://www.m8zworld.com"
        ),
        license = @License(
            name = "Apache 2.0", url = "https://www.m8zworld.com"
        ),
        termsOfService = "https://www.m8zworld.com",
        description = "API's for m8 shopping application"
    )
)
public class SwaggerConfig {
    
}
