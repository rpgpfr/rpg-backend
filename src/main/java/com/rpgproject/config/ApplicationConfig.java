package com.rpgproject.config;

import com.rpgproject.utils.IgnoreCoverage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@IgnoreCoverage
@Configuration
public class ApplicationConfig {

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {

		if (new File("/etc/secrets/.env").exists()) {
			PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
			configurer.setLocation(new FileSystemResource("/etc/secrets/.env"));
			return configurer;
		} else {
			System.out.println("hello");
			PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
			configurer.setLocation(new FileSystemResource(".env"));
			return configurer;
		}
	}

}
