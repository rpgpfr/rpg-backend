package com.rpgproject.application.presenter;

import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.UserProfileViewModel;
import com.rpgproject.domain.entity.UserProfile;
import com.rpgproject.domain.exception.CannotRegisterUserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static com.rpgproject.utils.CreationTestUtils.createUserProfile;
import static org.assertj.core.api.Assertions.assertThat;

class UserProfileRestPresenterTest {

	private UserProfileRestPresenter userProfileRestPresenter;

	@BeforeEach
	public void setUp() {
		userProfileRestPresenter = new UserProfileRestPresenter();
	}

	@Test
	@DisplayName("Should return a response with user profile")
	void shouldPresentAResponseWithUserProfile() {
		// Arrange
		UserProfile userProfile = createUserProfile();

		// Act
		ResponseEntity<ResponseViewModel<UserProfileViewModel>> userProfileViewModel = userProfileRestPresenter.ok(userProfile);

		// Assert
		ResponseEntity<ResponseViewModel<UserProfileViewModel>> expected = ResponseEntity.ok(
			new ResponseViewModel<>(new UserProfileViewModel(
				"username",
				null,
				null,
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
	void shouldReturnA400ResponseEntityWithAnErrorMessage() {
		// Arrange
		CannotRegisterUserException exception = new CannotRegisterUserException();

		// Act
		ResponseEntity<ResponseViewModel<UserProfileViewModel>> actualResponseEntity = userProfileRestPresenter.error(exception);

		// Assert
		ResponseEntity<ResponseViewModel<UserProfileViewModel>> expectedResponseEntity = ResponseEntity.badRequest().body(new ResponseViewModel<>(null, "An error occurred while registering the user"));

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

}