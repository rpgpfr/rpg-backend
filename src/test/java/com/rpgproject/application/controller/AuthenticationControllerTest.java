package com.rpgproject.application.controller;

import com.rpgproject.application.dto.requestbody.LoginRequestBody;
import com.rpgproject.application.dto.requestbody.RegisterRequestBody;
import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.UserViewModel;
import com.rpgproject.application.presenter.UserRestPresenter;
import com.rpgproject.application.service.ExceptionHTTPStatusService;
import com.rpgproject.domain.port.UserRepository;
import com.rpgproject.infrastructure.dao.UserJdbcDao;
import com.rpgproject.infrastructure.repository.UserJdbcRepository;
import com.rpgproject.utils.BasicDatabaseExtension;
import com.rpgproject.utils.EzDatabase;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.nio.file.Paths;
import java.util.HashMap;

import static java.nio.file.Files.readAllBytes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith({BasicDatabaseExtension.class, MockitoExtension.class})
class AuthenticationControllerTest {

	private AuthenticationController authenticationController;

	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@EzDatabase
	private NamedParameterJdbcTemplate jdbcTemplate;

	@BeforeEach
	void setUp() {
		UserJdbcDao userJdbcDao = new UserJdbcDao(jdbcTemplate);
		UserRepository userRepository = new UserJdbcRepository(userJdbcDao, bCryptPasswordEncoder);
		ExceptionHTTPStatusService exceptionHTTPStatusService = new ExceptionHTTPStatusService();
		UserRestPresenter userRestPresenter = new UserRestPresenter(exceptionHTTPStatusService);
		authenticationController = new AuthenticationController(userRepository, userRestPresenter);

		initTables();
	}

	@Test
	@DisplayName("Given a registerRequestBody, when the user does not exist, then it is registered")
	void givenARegisterRequestBody_whenTheUserDoesNotExist_thenItIsRegistered() {
		// Given
		RegisterRequestBody requestBody = new RegisterRequestBody("username", "mail2@example.com", "firstName", "lastName", "password");

		// When
		ResponseEntity<ResponseViewModel<UserViewModel>> actualResponse = authenticationController.registerUser(requestBody);

		// Then
		ResponseEntity<ResponseViewModel<UserViewModel>> expectedResponse = ResponseEntity.noContent().build();

		assertThat(actualResponse).isEqualTo(expectedResponse);
	}

	@Test
	@DisplayName("Given a loginRequestBody, when login is sucessful, then user is returned")
	void givenALoginRequestBody_whenLoginIsSuccessful_thenUserIsReturned() {
		// Given
		LoginRequestBody requestBody = new LoginRequestBody("alvin", "password");

		when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(true);

		// When
		ResponseEntity<ResponseViewModel<UserViewModel>> actualResponse = authenticationController.login(requestBody);

		// Then
		ResponseEntity<ResponseViewModel<UserViewModel>> expectedResponse = ResponseEntity.ok(new ResponseViewModel<>(new UserViewModel("alvin", "mail@example.com", "Alvin", "Hamaide", null, null), null));

		assertThat(actualResponse).isEqualTo(expectedResponse);
	}

	@SneakyThrows
	private void initTables() {
		jdbcTemplate.update(
			new String(readAllBytes(Paths.get("src/test/resources/initUsers.sql"))),
			new HashMap<>()
		);
	}

}