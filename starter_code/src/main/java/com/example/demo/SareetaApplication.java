package com.example.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableJpaRepositories("com.example.demo.model.persistence.repositories")
@EntityScan("com.example.demo.model.persistence")
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class SareetaApplication {

	/**
	 * Why JSON WebTokens are dangerous:
	 * -> https://redis.com/blog/json-web-tokens-jwt-are-dangerous-for-user-sessions/
	 *
	 * However, revoked JSON Web Tokens can also be a list and pushed to any server, creating additional security.
	 * E.g. Have a token carry a value of 5 minutes.
	 * When logging out black list the token and update the whole black list with any expired token anyways.
	 * So the list remains small(er).
	 * Push the list to the resource server.
	 */

	private static final Logger logger = LogManager.getLogger(SareetaApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SareetaApplication.class, args);
		logger.info("Ecommerce Application Started");
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}