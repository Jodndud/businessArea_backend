package com.businessArea.businessArea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class BusinessAreaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BusinessAreaApplication.class, args);
	}

}