package com.rpgproject.config;

import com.rpgproject.application.filter.JwtAuthFilter;
import com.rpgproject.utils.IgnoreCoverage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

import static org.springframework.security.config.Customizer.withDefaults;

@IgnoreCoverage
@Configuration
public class ApplicationSecurityConfig {

	@Value("${NEXTAUTH_SECRET}")
	private String secret;

	private final JwtAuthFilter jwtAuthFilter;

	public ApplicationSecurityConfig(JwtAuthFilter jwtAuthFilter) {
		this.jwtAuthFilter = jwtAuthFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.authorizeHttpRequests((authorize) ->
				authorize
					.requestMatchers("auth/**").permitAll()
					.anyRequest().authenticated()
			)
			.csrf(AbstractHttpConfigurer::disable)
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
			.cors(withDefaults())
			.oauth2ResourceServer((oauth2) ->
				oauth2.jwt(jwt -> jwt.decoder(jwtDecoder()))
			)
			.build();
	}

	@Bean
	public JwtDecoder jwtDecoder() {
		byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
		SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
		return NimbusJwtDecoder.withSecretKey(secretKey).build();
	}

}
