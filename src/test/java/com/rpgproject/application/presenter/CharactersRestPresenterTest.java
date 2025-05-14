package com.rpgproject.application.presenter;

import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.CharacterViewModel;
import com.rpgproject.application.service.ExceptionHTTPStatusService;
import com.rpgproject.domain.exception.InternalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.rpgproject.application.DTOCreationTestUtils.createCharacterViewModels;
import static com.rpgproject.domain.EntityCreationTestUtils.createCharacters;
import static org.assertj.core.api.Assertions.assertThat;

class CharactersRestPresenterTest {

	private CharactersRestPresenter charactersRestPresenter;

	@BeforeEach
	void setUp() {
		ExceptionHTTPStatusService exceptionHTTPStatusService = new ExceptionHTTPStatusService();
		this.charactersRestPresenter = new CharactersRestPresenter(exceptionHTTPStatusService);
	}

	@Test
	@DisplayName("Should return an empty response")
	void shouldReturnAnEmptyResponse() {
		// Act
		ResponseEntity<ResponseViewModel<List<CharacterViewModel>>> actualResponseEntity = charactersRestPresenter.ok();

		// Assert
		ResponseEntity<ResponseViewModel<List<CharacterViewModel>>> expectedResponseEntity = ResponseEntity.noContent().build();

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

	@Test
	@DisplayName("Given a character, when presenting it, then a response containing the character is returned")
	void givenACharacter_whenPresentingIt_thenAResponseContainingTheCharacterIsReturned() {
		// Given & When
		ResponseEntity<ResponseViewModel<List<CharacterViewModel>>> actualResponseEntity = charactersRestPresenter.ok(createCharacters());

		// Then
		ResponseEntity<ResponseViewModel<List<CharacterViewModel>>> expectedResponseEntity = ResponseEntity.ok(new ResponseViewModel<>(createCharacterViewModels(), null));

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

	@Test
	@DisplayName("Should return a response with an error message")
	void shouldReturnAResponseWithAnErrorMessage() {
		// Arrange
		InternalException exception = new InternalException("error");

		// Act
		ResponseEntity<ResponseViewModel<List<CharacterViewModel>>> actualResponseEntity = charactersRestPresenter.error(exception);

		// Assert
		ResponseEntity<ResponseViewModel<List<CharacterViewModel>>> expectedResponseEntity = ResponseEntity.internalServerError().body(new ResponseViewModel<>(null, "error"));

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

}