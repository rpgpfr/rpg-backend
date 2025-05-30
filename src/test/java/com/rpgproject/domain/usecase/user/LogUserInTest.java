package com.rpgproject.domain.usecase.user;

import com.rpgproject.domain.entity.User;
import com.rpgproject.domain.exception.InvalidCredentialsException;
import com.rpgproject.domain.port.Presenter;
import com.rpgproject.domain.port.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.rpgproject.domain.EntityCreationTestUtils.createUser;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogUserInTest {

	private LogUserIn<?> logUserIn;

	@Mock
	private UserRepository userRepository;

	@Mock
	private Presenter<User, ?> presenter;

	@BeforeEach
	void setUp() {
		logUserIn = new LogUserIn<>(userRepository, presenter);
	}

	@Test
	@DisplayName("Given an identifier and a password, when logging in, then user is presented")
	void givenAnIdentifierAndAPassword_whenLoggingIn_thenUserIsPresented() {
		// Given
		String identifier = "username";
		String password = "password";
		User user = createUser();

		when(userRepository.logIn(identifier, password)).thenReturn(user);

		// When
		logUserIn.execute(identifier, password);

		// Then
		verify(presenter, times(1)).ok(user);
	}

	@Test
	@DisplayName("Given an identifier and a password, when login fails because credentials are invalid, then an exception is presented")
	void givenAnIdentifierAndAPassword_whenLoginFailsBecauseCredentialsAreInvalid_thenAnExceptionIsPresented() {
		// Given
		String identifier = "username";
		String password = "password";
		InvalidCredentialsException exception = new InvalidCredentialsException("invalid credentials");

		when(userRepository.logIn(identifier, password)).thenThrow(exception);

		// When
		logUserIn.execute(identifier, password);

		// Then
		verify(presenter, times(1)).error(exception);
	}

}