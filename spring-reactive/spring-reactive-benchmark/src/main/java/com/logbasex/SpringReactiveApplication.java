package com.logbasex;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@Log4j2
@SpringBootApplication
public class SpringReactiveApplication {
	
	@PostConstruct
	public void init() {
		log.info("CPU: {}", Runtime.getRuntime().availableProcessors());
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SpringReactiveApplication.class, args);
	}
}
