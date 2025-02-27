package com.rpgproject.application.presenter;

import com.rpgproject.application.dto.responsebody.Response;
import com.rpgproject.application.dto.viewmodel.UserViewModel;
import com.rpgproject.domain.exception.CannotRegisterUserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class UserRestPresenterTest {

	private UserRestPresenter userRestPresenter;

	@BeforeEach
	public void setUp() {
		userRestPresenter = new UserRestPresenter();
	}

	@Test
	@DisplayName("Should return a 200 ResponseEntity")
	void shouldReturnA200ResponseEntity() {
		// Act
		ResponseEntity<Response<UserViewModel>> actualResponseEntity = userRestPresenter.ok();

		// Assert
		ResponseEntity<Response<UserViewModel>> expectedResponseEntity = ResponseEntity.status(HttpStatus.OK).build();

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

	@Test
	@DisplayName("Should return a 500 ResponseEntity with error message")
	void shouldReturnA500ResponseEntityWithErrorMessage() {
		// Arrange
		CannotRegisterUserException exception = new CannotRegisterUserException();

		// Act
		ResponseEntity<Response<UserViewModel>> actualResponseEntity = userRestPresenter.error(exception);

		// Assert
		ResponseEntity<Response<UserViewModel>> expectedResponseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response<>(null, "An error occurred while registering the user"));

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

}