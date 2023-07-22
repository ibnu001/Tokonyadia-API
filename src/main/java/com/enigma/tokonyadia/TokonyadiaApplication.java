package com.enigma.tokonyadia;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
		title = "Tokonyadia API",
		description = "Tokonyadia API for documentation",
		version = "1.0"
))
@SecurityScheme(name = "Authorization", scheme = "bearer", type = SecuritySchemeType.HTTP, bearerFormat = "JWT")
public class TokonyadiaApplication {
	public static void main(String[] args) {
		SpringApplication.run(TokonyadiaApplication.class, args);
	}
}
