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

import java.util.List;

import static com.rpgproject.domain.EntityCreationTestUtils.createCharacter;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateCharacterTest {

	private CreateCharacter<?> createCharacter;

	@Mock
	private CharacterRepository characterRepository;

	@Mock
	private Presenter<List<Character>, ?> characterPresenter;

	@BeforeEach
	void setUp() {
		this.createCharacter = new CreateCharacter<>(characterRepository, characterPresenter);
	}

	@Test
	@DisplayName("Given a character, when creating it, then a success is presented")
	void givenACharacter_whenCreatingIt_thenASuccessIsPresented() {
		// Given
		Character character = createCharacter();

		// When
		createCharacter.execute(character);

		// Then
		verify(characterRepository).save(character);
		verify(characterPresenter).ok();
	}

	@Test
	@DisplayName("Given a character, when creation fails, then an exception is presented")
	void givenACharacter_whenCreationFails_thenAnExceptionIsPresented() {
		// Given
		Character character = createCharacter();

		doThrow(InternalException.class).when(characterRepository).save(character);

		// When
		createCharacter.execute(character);

		// Then
		verify(characterRepository).save(character);
		verify(characterPresenter).error(any(InternalException.class));
	}

}