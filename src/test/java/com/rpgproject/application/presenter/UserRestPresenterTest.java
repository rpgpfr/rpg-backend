package com.rpgproject.application.presenter;

import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.UserViewModel;
import com.rpgproject.domain.entity.User;
import com.rpgproject.domain.exception.UserRegistrationFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static com.rpgproject.utils.CreationTestUtils.createUser;
import static com.rpgproject.utils.CreationTestUtils.createUserViewModel;
import static org.assertj.core.api.Assertions.assertThat;

class UserRestPresenterTest {

	private UserRestPresenter userRestPresenter;

	@BeforeEach
	public void setUp() {
		userRestPresenter = new UserRestPresenter();
	}

	@Test
	@DisplayName("Should return an empty response entity")
	void shouldReturnAnEmptyResponseEntity() {
		// Act
		ResponseEntity<ResponseViewModel<UserViewModel>> actualResponseEntity = userRestPresenter.ok();

		// Assert
		ResponseEntity<ResponseViewModel<UserViewModel>> expectedResponseEntity = ResponseEntity.ok().build();

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

	@Test
	@DisplayName("Should When")
	void shouldReturnAResponseWithAUserViewModel() {
		// Arrange
		User user = createUser();

		// Act
		ResponseEntity<ResponseViewModel<UserViewModel>> actualResponseEntity = userRestPresenter.ok(user);

		// Assert
		ResponseEntity<ResponseViewModel<UserViewModel>> expectdResponseEntity = ResponseEntity.ok(new ResponseViewModel<>(createUserViewModel(), null));
	}

	@Test
	@DisplayName("Should return a response with an error message")
	void shouldReturnAResponseWithAnErrorMessage() {
		// Arrange
		UserRegistrationFailedException exception = new UserRegistrationFailedException("L'utilisateur ou le mail associé est déjà utilisé.");

		// Act
		ResponseEntity<ResponseViewModel<UserViewModel>> actualResponseEntity = userRestPresenter.error(exception);

		// Assert
		ResponseEntity<ResponseViewModel<UserViewModel>> expectedResponseEntity = ResponseEntity.badRequest().body(new ResponseViewModel<>(null, "L'utilisateur ou le mail associé est déjà utilisé."));

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

}