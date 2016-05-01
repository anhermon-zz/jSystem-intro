package com.jethrodata.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

	public static void main(String[] args) {
		Collector.getInstance();
		SpringApplication.run(App.class, args);
		
	}
}
