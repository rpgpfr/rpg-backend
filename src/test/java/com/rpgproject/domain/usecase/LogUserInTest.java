package com.rpgproject.domain.usecase;

import com.rpgproject.domain.entity.User;
import com.rpgproject.domain.exception.UserLoginFailedException;
import com.rpgproject.domain.port.Presenter;
import com.rpgproject.domain.port.UserRepository;
import com.rpgproject.domain.usecase.user.LogUserIn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.rpgproject.utils.CreationTestUtils.createUser;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogUserInTest {

	private LogUserIn<?> logUserIn;

	@Mock
	private UserRepository userRepository;

	@Mock
	private Presenter<User, ?> presenter;

	@BeforeEach
	public void setUp() {
		logUserIn = new LogUserIn<>(userRepository, presenter);
	}

	@Test
	@DisplayName("Given an identifier and a password, when logging in, then user is returned")
	void givenAnIdentifierAndAPassword_whenLoggingIn_thenUserIsReturned() {
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
	@DisplayName("Given an identifier and a password, when login fails, then an error is returned")
	void givenAnIdentifierAndAPassword_whenLoginFails_thenAnErrorIsReturned() {
		// Given
		String identifier = "username";
		String password = "password";
		UserLoginFailedException exception = new UserLoginFailedException();

		when(userRepository.logIn(identifier, password)).thenThrow(exception);

		// When
		logUserIn.execute(identifier, password);

		// Then
		verify(presenter, times(1)).error(exception);
	}

}