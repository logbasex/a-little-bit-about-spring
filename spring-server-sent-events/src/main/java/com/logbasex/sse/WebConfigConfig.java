package com.logbasex.sse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//for CORS, outside application
@Configuration
public class WebConfigConfig {
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")  // Apply to all endpoints
						.allowedOrigins("*")  // Allow all origins
						.allowedMethods("*")  // Allow all methods
						.allowedHeaders("*"); // Allow all headers
			}
		};
	}
}
