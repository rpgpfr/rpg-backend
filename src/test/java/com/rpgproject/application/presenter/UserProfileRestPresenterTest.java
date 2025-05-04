package com.rpgproject.application.presenter;

import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.UserProfileViewModel;
import com.rpgproject.application.service.ExceptionHTTPStatusService;
import com.rpgproject.domain.entity.UserProfile;
import com.rpgproject.domain.exception.InternalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static com.rpgproject.domain.EntityCreationTestUtils.createUserProfile;
import static org.assertj.core.api.Assertions.assertThat;

class UserProfileRestPresenterTest {

	private UserProfileRestPresenter userProfileRestPresenter;
	private ExceptionHTTPStatusService exceptionHTTPStatusService;

	@BeforeEach
	void setUp() {
		this.exceptionHTTPStatusService = new ExceptionHTTPStatusService();
		this.userProfileRestPresenter = new UserProfileRestPresenter(exceptionHTTPStatusService);
	}

	@Test
	@DisplayName("Should return an empty response")
	void shouldReturnAnEmptyResponse() {
		// Act
		ResponseEntity<ResponseViewModel<UserProfileViewModel>> actualResponseEntity = userProfileRestPresenter.ok();

		// Assert
		ResponseEntity<ResponseViewModel<UserProfileViewModel>> expectedResponseEntity = ResponseEntity.noContent().build();

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

	@Test
	@DisplayName("Should return a response with a user profile")
	void shouldPresentAResponseWithAUserProfile() {
		// Arrange
		UserProfile userProfile = createUserProfile();

		// Act
		ResponseEntity<ResponseViewModel<UserProfileViewModel>> userProfileViewModel = userProfileRestPresenter.ok(userProfile);

		// Assert
		ResponseEntity<ResponseViewModel<UserProfileViewModel>> expected = ResponseEntity.ok(
			new ResponseViewModel<>(new UserProfileViewModel(
				"username",
				"mail@example2.com",
				"firstName",
				"lastName",
				"01/01/2025",
				null,
				null,
				1,
				1,
				1,
				3
			), null)
		);

		assertThat(userProfileViewModel).isEqualTo(expected);
	}

	@Test
	@DisplayName("Should return a response with an error message")
	void shouldReturnAResponseWithAnErrorMessage() {
		// Arrange
		InternalException exception = new InternalException("error");

		// Act
		ResponseEntity<ResponseViewModel<UserProfileViewModel>> actualResponseEntity = userProfileRestPresenter.error(exception);

		// Assert
		ResponseEntity<ResponseViewModel<UserProfileViewModel>> expectedResponseEntity = ResponseEntity.internalServerError().body(new ResponseViewModel<>(null, "error"));

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

}