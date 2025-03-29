package com.rpgproject.domain.usecase.user;

import com.rpgproject.domain.entity.User;
import com.rpgproject.domain.exception.UserUpdateFailedException;
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
class UpdateUserTest {

	private UpdateUser<?> updateUser;

	@Mock
	private UserRepository userRepository;

	@Mock
	private Presenter<User, ?> presenter;

	@BeforeEach
	public void setUp() {
		updateUser = new UpdateUser<>(userRepository, presenter);
	}

	@Test
	@DisplayName("Given a user, when updating, then present success")
	void givenAUsername_whenUserIsRegistered_thenPresentSuccess() {
		// Given
		User user = createUser();

		// When
		updateUser.execute(user);

		// Then
		verify(presenter, times(1)).ok(user);
	}

	@Test
	@DisplayName("Given a user, when update throw an exception, then present error")
	void givenAUsername_whenRegisterThrowAnException_thenPresentError() {
		// Given
		User user = createUser();
		RuntimeException exception = new UserUpdateFailedException();

		doThrow(exception).when(userRepository).update(user);

		// When
		updateUser.execute(user);

		// Then
		verify(presenter, times(1)).error(exception);
	}

}