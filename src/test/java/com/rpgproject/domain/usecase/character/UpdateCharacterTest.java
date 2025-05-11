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
class UpdateCharacterTest {

	private UpdateCharacter<?> updateCharacter;

	@Mock
	private CharacterRepository characterRepository;

	@Mock
	private Presenter<Character, ?> characterPresenter;

	@BeforeEach
	void setUp() {
		this.updateCharacter = new UpdateCharacter<>(characterRepository, characterPresenter);
	}

	@Test
	@DisplayName("Given a character, when updating it, then a success is presented")
	void givenACharacter_whenCreatingIt_thenASuccessIsPresented() {
		// Given
		Character character = createCharacter();
		String oldName = "oldName";

		// When
		updateCharacter.execute(character, oldName);

		// Then
		String expectedOldName = "oldName";
		verify(characterRepository).update(character, expectedOldName);
		verify(characterPresenter).ok();
	}

	@Test
	@DisplayName("Given a character, when update fails, then an exception is presented")
	void givenACharacter_whenCreationFails_thenAnExceptionIsPresented() {
		// Given
		Character character = createCharacter();
		String oldName = "oldName";

		doThrow(InternalException.class).when(characterRepository).update(character, oldName);

		// When
		updateCharacter.execute(character, oldName);

		// Then
		String expectedOldName = "oldName";
		verify(characterRepository).update(character, expectedOldName);
		verify(characterPresenter).error(any(InternalException.class));
	}

}