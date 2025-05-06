package com.rpgproject.infrastructure.dao;

import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.rpgproject.infrastructure.dto.CampaignDTO;
import com.rpgproject.infrastructure.dto.CharacterDTO;
import com.rpgproject.infrastructure.dto.QuestDTO;
import com.rpgproject.infrastructure.exception.charactermongo.CharacterNotFoundException;
import com.rpgproject.infrastructure.exception.charactermongo.DuplicateCharacterNameException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.rpgproject.infrastructure.DTOCreationTestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DataMongoTest
@ActiveProfiles("test")
class CharacterMongoDaoTest {

	private CharacterMongoDao characterMongoDao;

	@Autowired
	private MongoTemplate mongoTemplate;

	@BeforeEach
	void setUp() {
		characterMongoDao = new CharacterMongoDao(mongoTemplate);

		mongoTemplate.createCollection("Character");

		initializeMongoDB();
	}

	private void initializeMongoDB() {
		IndexOptions indexOptions = new IndexOptions().unique(true);

		mongoTemplate.createCollection("Character")
			.createIndex(
				Indexes.compoundIndex(
					Indexes.ascending("campaignSlug"),
					Indexes.ascending("owner"),
					Indexes.ascending("name")
				),
				indexOptions
			);

		mongoTemplate.insert(createCharacterDTOs(), "Character");
	}

	@AfterEach
	void tearDown() {
		mongoTemplate.dropCollection("Character");
	}

	@Test
	@DisplayName("Given a campaign slug and an owner, when getting the characters, then they are returned")
	void givenACampaignSlugAndAnOwner_whenGettingTheCharacters_thenTheyAreReturned() {
		// Given
		CampaignDTO campaignDTO = createCampaignDTOs().getFirst();
		String campaignSlug = campaignDTO.getSlug();
		String owner = campaignDTO.getOwner();

		// When
		List<CharacterDTO> actualCharacters = characterMongoDao.getCharactersByCampaignsSlugAndOwner(campaignSlug, owner);

		// Then
		List<CharacterDTO> expectedCharacters = List.of(createCharacterDTOs().getFirst());

		assertThat(actualCharacters).isEqualTo(expectedCharacters);
	}

	@Test
	@DisplayName("Given an owner, when getting the number of characters created by the user, then the count is returned")
	void givenAnOwner_whenGettingTheNumberOfCharactersCreatedByTheUser_thenTheCountIsReturned() {
		// Given
		String owner = "username";

		// When
		long actualCount = characterMongoDao.getCountByOwner(owner);

		// Then
		long expectedCount = 3;

		assertThat(actualCount).isEqualTo(expectedCount);
	}

	@Test
	@DisplayName("Given a characterDTO, when it does not exist, then it is saved")
	void givenACharacterDTO_whenItDoesNotExist_thenItIsSaved() {
		// Given
		CharacterDTO characterDTO = createCharacterDTO();

		// When & Then
		assertThatCode(() -> characterMongoDao.save(characterDTO)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a characterDTO, when save fails because it already exists, then an exception is thrown")
	void givenACharacterDTO_whenSaveFailsBecauseItAlreadyExists_thenAnExceptionIsThrown() {
		// Given
		CharacterDTO characterDTO = createCharacterDTOs().getFirst();

		// When & Then
		assertThatCode(() -> characterMongoDao.save(characterDTO)).isInstanceOf(DuplicateCharacterNameException.class);
	}

	@Test
	@DisplayName("Given a characterDTO, when save fails, then an exception is thrown")
	void givenACharacterDTO_whenSaveFails_thenAnExceptionIsThrown() {
		// Given & When & Then
		assertThatCode(() -> characterMongoDao.save(null))
			.isInstanceOf(RuntimeException.class)
			.hasMessage("Une erreur est survenue lors de la création du personnage.");
	}

	@Test
	@DisplayName("Given a characterDTO and its old name, when it exists, then it is updated")
	void givenACharacterDTOAndItsOldName_whenItExists_thenItIsUpdated() {
		// Given
		CharacterDTO characterDTO = createCharacterDTOs().getFirst();
		CharacterDTO updatedCharacterDTO = new CharacterDTO(characterDTO.getOwner(), characterDTO.getCampaignSlug(), "newName");

		// When & Then
		assertThatCode(() -> characterMongoDao.update(updatedCharacterDTO, characterDTO.getName())).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a characterDTO and its old name, when update fails because a character with the same name already exists, then an exception is thrown")
	void givenACharacterDTOAndItsOldName_whenUpdateFailsBecauseACharacterWithTheSameNameAlreadyExists_thenAnExceptionIsThrown() {
		// Given
		CharacterDTO characterDTO = createCharacterDTO();
		CharacterDTO characterDTOToUpdate = new CharacterDTO(
			characterDTO.getOwner(),
			characterDTO.getCampaignSlug(),
			"oldName"
		);

		mongoTemplate.insert(characterDTOToUpdate, "Character");
		mongoTemplate.insert(characterDTO, "Character");

		String oldName = characterDTOToUpdate.getName();

		// When & Then
		assertThatCode(() -> characterMongoDao.update(characterDTO, oldName)).isInstanceOf(DuplicateCharacterNameException.class);
	}

	@Test
	@DisplayName("Given a characterDTO and its old name, when update fails because it is not found, then an exception is thrown")
	void givenACharacterDTOAndItsOldName_whenUpdateFailsBecauseItIsNotFound_thenAnExceptionIsThrown() {
		// Given
		CharacterDTO characterDTO = createCharacterDTO();
		String oldName = characterDTO.getName();

		// When & Then
		assertThatCode(() -> characterMongoDao.update(characterDTO, oldName)).isInstanceOf(CharacterNotFoundException.class);
	}

	@Test
	@DisplayName("Given a characterDTO and its old name, when update fails, then an exception is thrown")
	void givenACharacterDTOAndItsOldName_whenUpdateFails_thenAnExceptionIsThrown() {
		// Given & When & Then
		assertThatCode(() -> characterMongoDao.update(null, null))
			.isInstanceOf(RuntimeException.class)
			.hasMessage("Une erreur est survenue lors de la mise à jour des informations.");
	}

	@Test
	@DisplayName("Should delete all characters related to campaignSlug and owner")
	void shouldDeleteAllCharactersRelatedToCampaignSlugAndOwner() {
		// Given
		QuestDTO questDTO = createQuestDTOs().getFirst();
		String campaignSlug = questDTO.getCampaignSlug();
		String owner = questDTO.getOwner();

		// When & Then
		assertThatCode(() -> characterMongoDao.deleteAllByCampaignSlugAndOwner(campaignSlug, owner)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a characterDTO, when it exists, then it is deleted")
	void givenACharacterDTO_whenItExists_thenItIsDeleted() {
		// Given
		CharacterDTO characterDTO = createCharacterDTOs().getFirst();

		// When & Then
		assertThatCode(() -> characterMongoDao.delete(characterDTO)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a characterDTO, when delete fails because it is not found, then an exception is thrown")
	void givenACharacterDTO_whenDeleteFailsBecauseItIsNotFound_thenAnExceptionIsThrown() {
		// Given
		CharacterDTO characterDTO = new CharacterDTO("wrong owner", "wrong slug", "wrong name");

		// When & Then
		assertThatCode(() -> characterMongoDao.delete(characterDTO)).isInstanceOf(CharacterNotFoundException.class);
	}

	@Test
	@DisplayName("Given a characterDTO, when delete fails, then an exception is thrown")
	void givenAWrongCharacterDTO_whenDeleteFails_thenAnExceptionIsThrown() {
		// Given & When & Then
		assertThatCode(() -> characterMongoDao.delete(null))
			.isInstanceOf(RuntimeException.class)
			.hasMessage("Une erreur est survenue lors de la suppression du personnage.");
	}

}