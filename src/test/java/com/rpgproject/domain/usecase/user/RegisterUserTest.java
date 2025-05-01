package com.rpgproject.domain.usecase.user;

import com.rpgproject.domain.entity.User;
import com.rpgproject.domain.exception.DuplicateException;
import com.rpgproject.domain.exception.InternalException;
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
	@DisplayName("Given a user, when it is registered, then a success is presented")
	void givenAUser_whenItIsRegistered_thenASuccessIsPresented() {
		// Given
		User user = createUser();

		// When
		registerUser.execute(user);

		// Then
		verify(presenter, times(1)).ok();
	}

	@Test
	@DisplayName("Given a user, when register fails, then an exception is presented")
	void givenAUsername_whenItRegisterFails_thenAnExceptionIsPresented() {
		// Given
		User user = createUser();
		InternalException exception = new InternalException("error");

		doThrow(exception).when(userRepository).register(user);

		// When
		registerUser.execute(user);

		// Then
		verify(presenter, times(1)).error(exception);
	}

}