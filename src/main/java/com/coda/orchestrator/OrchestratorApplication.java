package com.coda.orchestrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

/**
 * @author prasad
 */
@SpringBootApplication
@EnableScheduling
public class OrchestratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrchestratorApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
