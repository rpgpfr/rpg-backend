package com.rpgproject.domain.usecase.user;

import com.rpgproject.domain.entity.User;
import com.rpgproject.domain.exception.user.UserRegistrationFailedException;
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
class RegisterUserTest {

	private RegisterUser<?> registerUser;

	@Mock
	private UserRepository userRepository;

	@Mock
	private Presenter<User, ?> presenter;

	@BeforeEach
	public void setUp() {
		registerUser = new RegisterUser<>(userRepository, presenter);
	}

	@Test
	@DisplayName("Given a user, when user is registered, then present success")
	void givenAUsername_whenUserIsRegistered_thenPresentSuccess() {
		// Given
		User user = createUser();

		// When
		registerUser.execute(user);

		// Then
		verify(presenter, times(1)).ok();
	}

	@Test
	@DisplayName("Given a user, when register throw an exception, then present error")
	void givenAUsername_whenRegisterThrowAnException_thenPresentError() {
		// Given
		User user = createUser();
		RuntimeException exception = new UserRegistrationFailedException("L'utilisateur ou le mail associé est déjà utilisé.");

		doThrow(exception).when(userRepository).register(user);

		// When
		registerUser.execute(user);

		// Then
		verify(presenter, times(1)).error(exception);
	}

}