package com.endava.workshops.restexample.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({
	"com.endava.workshops.restexample.infrastructure",
	"com.endava.workshops.restexample.application"})
public class RestExampleApplication {
	public static void main(String[] args) {
		SpringApplication.run(RestExampleApplication.class, args);
	}
}
