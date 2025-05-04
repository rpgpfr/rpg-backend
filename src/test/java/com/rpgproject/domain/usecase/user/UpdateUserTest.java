package com.rpgproject.domain.usecase.user;

import com.rpgproject.domain.entity.User;
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
class UpdateUserTest {

	private UpdateUser<?> updateUser;

	@Mock
	private UserRepository userRepository;

	@Mock
	private Presenter<User, ?> presenter;

	@BeforeEach
	void setUp() {
		updateUser = new UpdateUser<>(userRepository, presenter);
	}

	@Test
	@DisplayName("Given a user, when it is updated, then a success is presented")
	void givenAUser_whenItIsUpdated_thenASuccessIsPresented() {
		// Given
		User user = createUser();

		// When
		updateUser.execute(user);

		// Then
		verify(presenter, times(1)).ok(user);
	}

	@Test
	@DisplayName("Given a user, when update fails, then an exception is presented")
	void givenAUser_whenUpdateFails_thenAnExceptionIsPresented() {
		// Given
		User user = createUser();
		InternalException exception = new InternalException("error");

		doThrow(exception).when(userRepository).update(user);

		// When
		updateUser.execute(user);

		// Then
		verify(presenter, times(1)).error(exception);
	}

}