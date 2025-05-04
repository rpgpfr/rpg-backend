package com.rpgproject.application.presenter;

import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.UserViewModel;
import com.rpgproject.application.service.ExceptionHTTPStatusService;
import com.rpgproject.domain.entity.User;
import com.rpgproject.domain.exception.InternalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static com.rpgproject.application.DTOCreationTestUtils.createUserViewModel;
import static com.rpgproject.domain.EntityCreationTestUtils.createUser;
import static org.assertj.core.api.Assertions.assertThat;

class UserRestPresenterTest {

	private UserRestPresenter userRestPresenter;
	private ExceptionHTTPStatusService exceptionHTTPStatusService;

	@BeforeEach
	void setUp() {
		exceptionHTTPStatusService = new ExceptionHTTPStatusService();
		userRestPresenter = new UserRestPresenter(exceptionHTTPStatusService);
	}

	@Test
	@DisplayName("Should return an empty response")
	void shouldReturnAnEmptyResponse() {
		// Act
		ResponseEntity<ResponseViewModel<UserViewModel>> actualResponseEntity = userRestPresenter.ok();

		// Assert
		ResponseEntity<ResponseViewModel<UserViewModel>> expectedResponseEntity = ResponseEntity.noContent().build();

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

	@Test
	@DisplayName("Should return a response with a user")
	void shouldReturnAResponseWithAUser() {
		// Arrange
		User user = createUser();

		// Act
		ResponseEntity<ResponseViewModel<UserViewModel>> actualResponseEntity = userRestPresenter.ok(user);

		// Assert
		ResponseEntity<ResponseViewModel<UserViewModel>> expectdResponseEntity = ResponseEntity.ok(new ResponseViewModel<>(createUserViewModel(), null));

		assertThat(actualResponseEntity).isEqualTo(expectdResponseEntity);
	}

	@Test
	@DisplayName("Should return a response with an error message")
	void shouldReturnAResponseWithAnErrorMessage() {
		// Arrange
		InternalException exception = new InternalException("error");

		// Act
		ResponseEntity<ResponseViewModel<UserViewModel>> actualResponseEntity = userRestPresenter.error(exception);

		// Assert
		ResponseEntity<ResponseViewModel<UserViewModel>> expectedResponseEntity = ResponseEntity.internalServerError().body(new ResponseViewModel<>(null, "error"));

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

}