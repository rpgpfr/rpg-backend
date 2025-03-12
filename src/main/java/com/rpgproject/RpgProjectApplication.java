package com.rpgproject;

import com.rpgproject.utils.IgnoreCoverage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@IgnoreCoverage
@SpringBootApplication
public class RpgProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(RpgProjectApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
