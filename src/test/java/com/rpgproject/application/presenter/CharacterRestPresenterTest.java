package com.rpgproject.application.presenter;

import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.CharacterViewModel;
import com.rpgproject.application.service.ExceptionHTTPStatusService;
import com.rpgproject.domain.entity.Character;
import com.rpgproject.domain.exception.InternalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static com.rpgproject.application.DTOCreationTestUtils.createCharacterViewModel;
import static com.rpgproject.domain.EntityCreationTestUtils.createCharacter;
import static org.assertj.core.api.Assertions.assertThat;

class CharacterRestPresenterTest {

	private CharacterRestPresenter characterRestPresenter;

	@BeforeEach
	void setUp() {
		ExceptionHTTPStatusService exceptionHTTPStatusService = new ExceptionHTTPStatusService();
		this.characterRestPresenter = new CharacterRestPresenter(exceptionHTTPStatusService);
	}

	@Test
	@DisplayName("Should return an empty response")
	void shouldReturnAnEmptyResponse() {
		// Act
		ResponseEntity<ResponseViewModel<CharacterViewModel>> actualResponseEntity = characterRestPresenter.ok();

		// Assert
		ResponseEntity<ResponseViewModel<CharacterViewModel>> expectedResponseEntity = ResponseEntity.noContent().build();

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

	@Test
	@DisplayName("Given a character, when presenting it, then a response containing the character is returned")
	void givenACharacter_whenPresentingIt_thenAResponseContainingTheCharacterIsReturned() {
		// Given
		Character character = createCharacter();

		// When
		ResponseEntity<ResponseViewModel<CharacterViewModel>> actualResponseEntity = characterRestPresenter.ok(character);

		// Then
		ResponseEntity<ResponseViewModel<CharacterViewModel>> expectedResponseEntity = ResponseEntity.ok(new ResponseViewModel<>(createCharacterViewModel(), null));

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

	@Test
	@DisplayName("Should return a response with an error message")
	void shouldReturnAResponseWithAnErrorMessage() {
		// Arrange
		InternalException exception = new InternalException("error");

		// Act
		ResponseEntity<ResponseViewModel<CharacterViewModel>> actualResponseEntity = characterRestPresenter.error(exception);

		// Assert
		ResponseEntity<ResponseViewModel<CharacterViewModel>> expectedResponseEntity = ResponseEntity.internalServerError().body(new ResponseViewModel<>(null, "error"));

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

}