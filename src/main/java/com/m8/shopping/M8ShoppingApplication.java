package com.m8.shopping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.m8.shopping.config.RsaKeyProperties;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
@SecurityScheme(name = "shopping-api",scheme = "bearer",type = SecuritySchemeType.HTTP,in = SecuritySchemeIn.HEADER)
public class M8ShoppingApplication {

	public static void main(String[] args) {
		SpringApplication.run(M8ShoppingApplication.class, args);
	}
	//develop branch push
}
