package com.bridgelabz.fundoo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FundooApplication {

	public static void main(String[] args) {
		SpringApplication.run(FundooApplication.class, args);
	}

}
