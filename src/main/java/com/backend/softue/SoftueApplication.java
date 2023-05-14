package com.backend.softue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SoftueApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoftueApplication.class, args);
	}

}
