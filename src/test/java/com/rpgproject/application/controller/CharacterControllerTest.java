package com.rpgproject.application.controller;

import com.rpgproject.application.dto.requestbody.CharacterRequestBody;
import com.rpgproject.application.dto.requestbody.CharacterUpdateRequestBody;
import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.CharacterViewModel;
import com.rpgproject.domain.entity.Campaign;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.rpgproject.application.DTOCreationTestUtils.createCharacterViewModels;
import static com.rpgproject.domain.EntityCreationTestUtils.createCampaigns;
import static com.rpgproject.infrastructure.DTOCreationTestUtils.createCharacterDTOs;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class CharacterControllerTest {

	@Autowired
	private CharacterController characterController;

	@Autowired
	private MongoTemplate mongoTemplate;

	@BeforeEach
	void setUp() {
		mongoTemplate.insert(createCharacterDTOs(), "Character");
	}

	@AfterEach
	void tearDown() {
		mongoTemplate.dropCollection("Character");
	}

	@Test
	@DisplayName("Given a campaign slug and an owner, when getting the characters, then all of them are returned")
	void givenACampaignSlugAndAnOwner_whenGettingTheCharacters_thenAllOfThemAreReturned() {
		// Given
		Campaign campaign = createCampaigns().getFirst();
		String slug = campaign.getSlug();
		String owner = campaign.getOwner();

		// When
		ResponseEntity<ResponseViewModel<List<CharacterViewModel>>> actualResponse = characterController.getCharacters(owner, slug);

		// Then
		ResponseEntity<ResponseViewModel<List<CharacterViewModel>>> expectedResponse = ResponseEntity.ok(
			new ResponseViewModel<>(
				List.of(createCharacterViewModels().getFirst()),
				null
			)
		);

		assertThat(actualResponse).isEqualTo(expectedResponse);
	}

	@Test
	@DisplayName("Given a characterRequestBody with a campaign slug and an owner, when the character does not exist, then it is saved")
	void givenACharacterRequestBodyWithACampaignSlugAndAnOwner_whenTheCharacterDoesNotExist_thenItIsSaved() {
		// Given
		CharacterRequestBody characterRequestBody = new CharacterRequestBody("character 1");
		String owner = "username";
		String slug = "my-campaign";

		// When
		ResponseEntity<ResponseViewModel<List<CharacterViewModel>>> actualResponse = characterController.createCharacter(owner, slug, characterRequestBody);

		// Then
		ResponseEntity<ResponseViewModel<List<CharacterViewModel>>> expectedResponse = ResponseEntity.noContent().build();

		assertThat(actualResponse).isEqualTo(expectedResponse);
	}

	@Test
	@DisplayName("Given a characterUpdateRequestBody with a campaign slug and an owner, when the character exists, then it is updated")
	void givenACharacterUpdateRequestBodyWithACampaignSlugAndAnOwner_whenTheCharacterExists_thenItIsUpdated() {
		// Given
		CharacterUpdateRequestBody characterUpdateRequestBody = new CharacterUpdateRequestBody("character 1", "new name");
		String owner = "username";
		String slug = "campagne-1";

		// When
		ResponseEntity<ResponseViewModel<List<CharacterViewModel>>> actualResponse = characterController.updateCharacter(owner, slug, characterUpdateRequestBody);

		// Then
		ResponseEntity<ResponseViewModel<List<CharacterViewModel>>> expectedResponse = ResponseEntity.noContent().build();

		assertThat(actualResponse).isEqualTo(expectedResponse);
	}

	@Test
	@DisplayName("Given a character name with a campaign slug and an owner, when the character exists, then it is deleted")
	void givenACharacterNameWithACampaignSlugAndAnOwner_whenTheCharacterExists_thenItIsDelete() {
		// Given
		String characterName = "character 1";
		String owner = "username";
		String slug = "campagne-1";

		// When
		ResponseEntity<ResponseViewModel<List<CharacterViewModel>>> actualResponse = characterController.deleteCharacter(owner, slug, characterName);

		// Then
		ResponseEntity<ResponseViewModel<List<CharacterViewModel>>> expectedResponse = ResponseEntity.noContent().build();

		assertThat(actualResponse).isEqualTo(expectedResponse);
	}

}