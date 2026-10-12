package edu.co.udes.spafeu.web.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpafeuWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpafeuWebApplication.class, args);
	}

}
