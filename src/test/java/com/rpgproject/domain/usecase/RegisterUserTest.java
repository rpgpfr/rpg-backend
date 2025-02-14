package com.rpgproject.domain.usecase;

import com.rpgproject.domain.bean.User;
import com.rpgproject.domain.exception.CannotRegisterUserException;
import com.rpgproject.domain.port.UserPresenter;
import com.rpgproject.domain.port.UserRepository;
import com.rpgproject.utils.CreationTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserTest {

	private RegisterUser<?> registerUser;

	@Mock
	private UserRepository userRepository;

	@Mock
	private UserPresenter<?> userPresenter;

	@BeforeEach
	public void setUp() {
		registerUser = new RegisterUser<>(userRepository, userPresenter);
	}

	@Test
	@DisplayName("Given a user When user is registered Then present success")
	void givenAUser_whenUserisRegistered_thenPresentSuccess() {
		// Given
		User user = CreationTestUtils.createUser();

		// When
		registerUser.execute(user);

		// Then
		verify(userPresenter, times(1)).ok();
	}

	@Test
	@DisplayName("Given a user When register throw an exception Then present error")
	void givenAUser_whenRegisterThrowAnException_thenPresentError() {
		// Given
		User user = CreationTestUtils.createUser();
		RuntimeException exception = new CannotRegisterUserException();

		doThrow(exception).when(userRepository).register(user);

		// When
		registerUser.execute(user);

		// Then
		verify(userPresenter, times(1)).error(exception);
	}

}