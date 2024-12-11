package com.orioninc.statsfabric;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@ComponentScan("com.orioninc.statsfabric.configuration")
@EnableJpaRepositories("com.orioninc.statsfabric.repository")
@EntityScan(basePackages = "com.orioninc.statsfabric.entities")
public class StatsfabricApplication {

	public static void main(String[] args) {
		SpringApplication.run(StatsfabricApplication.class, args);
	}

}
