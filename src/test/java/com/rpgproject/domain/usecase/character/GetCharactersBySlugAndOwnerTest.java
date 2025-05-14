package com.rpgproject.domain.usecase.character;

import com.rpgproject.domain.entity.Character;
import com.rpgproject.domain.port.CharacterRepository;
import com.rpgproject.domain.port.Presenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.rpgproject.domain.EntityCreationTestUtils.createCharacters;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCharactersBySlugAndOwnerTest {

	private GetCharactersBySlugAndOwner<?> getCharactersBySlugAndOwner;

	@Mock
	private CharacterRepository characterRepository;

	@Mock
	private Presenter<List<Character>, ?> presenter;

	@BeforeEach
	void setUp() {
		this.getCharactersBySlugAndOwner = new GetCharactersBySlugAndOwner<>(characterRepository, presenter);
	}

	@Test
	@DisplayName("Given a campaign slug and an owner, when getting the characters, then all of them are presented")
	void givenACampaignSlugAndOwner_whenGettingTheCharacters_thenAllOfThemArePresented() {
		// Given
		List<Character> characters = createCharacters();
		String owner = "owner";
		String campaignSlug = "campaign-slug";

		when(characterRepository.getCharactersByCampaignsSlugAndOwner(campaignSlug, owner)).thenReturn(characters);

		// When
		getCharactersBySlugAndOwner.execute(campaignSlug, owner);

		// Then
		verify(characterRepository).getCharactersByCampaignsSlugAndOwner(campaignSlug, owner);
		verify(presenter).ok(characters);
	}

}