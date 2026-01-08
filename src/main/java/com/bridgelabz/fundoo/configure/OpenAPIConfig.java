package com.bridgelabz.fundoo.configure;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    OpenAPI fundooOpenAPI() {
	return new OpenAPI().info(new Info().title("Spring Boot REST API").description("Fundoo Notes User REST API")
		.version("1.0.0").contact(new Contact().name("Gautam Singh").url("https://www.github.com/gautamiaryan")
			.email("gautamiaryan@gmail.com")));
    }

}
