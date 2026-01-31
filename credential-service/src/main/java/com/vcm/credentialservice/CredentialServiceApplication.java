package com.vcm.credentialservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CredentialServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CredentialServiceApplication.class, args);
	}

}
