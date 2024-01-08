package com.js.secondhandauction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SecondhandauctionApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecondhandauctionApplication.class, args);
	}

}
