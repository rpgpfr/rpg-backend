package com.rpgproject.domain.usecase.character;

import com.rpgproject.domain.entity.Character;
import com.rpgproject.domain.exception.InternalException;
import com.rpgproject.domain.port.CharacterRepository;
import com.rpgproject.domain.port.Presenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.rpgproject.domain.EntityCreationTestUtils.createCharacter;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteCharacterTest {

	private DeleteCharacter<?> deleteCharacter;

	@Mock
	private CharacterRepository characterRepository;

	@Mock
	private Presenter<Character, ?> characterPresenter;

	@BeforeEach
	void setUp() {
		this.deleteCharacter = new DeleteCharacter<>(characterRepository, characterPresenter);
	}

	@Test
	@DisplayName("Given a character, when creating it, then a success is presented")
	void givenACharacter_whenCreatingIt_thenASuccessIsPresented() {
		// Given
		Character character = createCharacter();

		// When
		deleteCharacter.execute(character);

		// Then
		verify(characterRepository).delete(character);
		verify(characterPresenter).ok();
	}

	@Test
	@DisplayName("Given a character, when creation fails, then an exception is presented")
	void givenACharacter_whenCreationFails_thenAnExceptionIsPresented() {
		// Given
		Character character = createCharacter();

		doThrow(InternalException.class).when(characterRepository).delete(character);

		// When
		deleteCharacter.execute(character);

		// Then
		verify(characterRepository).delete(character);
		verify(characterPresenter).error(any(InternalException.class));
	}

}