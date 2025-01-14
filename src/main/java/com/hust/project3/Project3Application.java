package com.hust.project3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Project3Application {

	public static void main(String[] args) {
		SpringApplication.run(Project3Application.class, args);
	}

}
