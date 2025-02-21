package com.rpgproject.config.security;

import com.rpgproject.utils.IgnoreCoverage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.oauth2.jwt.JwtDecoders.fromOidcIssuerLocation;
import static org.springframework.security.oauth2.jwt.JwtValidators.createDefaultWithIssuer;

@IgnoreCoverage
@Configuration
public class ApplicationSecurityConfig {

	@Value("${AUTH0_AUDIENCE}")
	private String audience;

	@Value("${AUTH0_ISSUER}")
	private String issuer;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.authorizeHttpRequests((authorize) ->
				authorize
					.requestMatchers("/register").permitAll()
					.anyRequest().authenticated()
			)
			.cors(withDefaults())
			.oauth2ResourceServer((oauth2) ->
				oauth2.jwt((jwt) ->
					jwt.decoder(jwtDecoder())
				)
			)
			.build();
	}

	@Bean
	public JwtDecoder jwtDecoder() {
		OAuth2TokenValidator<Jwt> withAudience = new AudienceValidator(audience);
		OAuth2TokenValidator<Jwt> withIssuer = createDefaultWithIssuer(issuer);
		OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(withAudience, withIssuer);

		NimbusJwtDecoder jwtDecoder = fromOidcIssuerLocation(issuer);
		jwtDecoder.setJwtValidator(validator);

		return jwtDecoder;
	}

}
