package com.rpgproject.domain.usecase;

import com.rpgproject.domain.entity.User;
import com.rpgproject.domain.exception.UserLoginFailedException;
import com.rpgproject.domain.port.Presenter;
import com.rpgproject.domain.port.UserRepository;
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
	@DisplayName("Given a user with username, when logging in, then user is returned")
	void givenAUserWithUsername_whenLoggingIn_thenUserIsReturned() {
		// Given
		User requestedUser = new User("username", null, null, null, "password");
		User user = createUser();

		when(userRepository.logIn(requestedUser)).thenReturn(user);

		// When
		logUserIn.execute(requestedUser);

		// Then
		verify(presenter, times(1)).ok(user);
	}

	@Test
	@DisplayName("Given a user with email, when logging in, then user is returned")
	void givenAUserWithEmail_whenLoggingIn_thenUserIsReturned() {
		// Given
		User requestedUser = new User(null, "email", null, null, "password");
		User user = createUser();

		when(userRepository.logIn(requestedUser)).thenReturn(user);

		// When
		logUserIn.execute(requestedUser);

		// Then
		verify(presenter, times(1)).ok(user);
	}

	@Test
	@DisplayName("Given a user, when login fails, then an error is returned")
	void givenAUserWithEmail_whenLoginFails_thenAnErrorIsReturned() {
		// Given
		User requestedUser = new User(null, "email", null, null, "password");
		UserLoginFailedException exception = new UserLoginFailedException();

		when(userRepository.logIn(requestedUser)).thenThrow(exception);

		// When
		logUserIn.execute(requestedUser);

		// Then
		verify(presenter, times(1)).error(exception);
	}

}