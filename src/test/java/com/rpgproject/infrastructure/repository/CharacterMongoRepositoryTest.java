package com.rpgproject.infrastructure.repository;

import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.entity.Character;
import com.rpgproject.domain.exception.DuplicateException;
import com.rpgproject.domain.exception.InternalException;
import com.rpgproject.domain.exception.NotFoundException;
import com.rpgproject.infrastructure.dao.CharacterMongoDao;
import com.rpgproject.infrastructure.dto.QuestDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.rpgproject.domain.EntityCreationTestUtils.*;
import static com.rpgproject.infrastructure.DTOCreationTestUtils.createCharacterDTOs;
import static com.rpgproject.infrastructure.DTOCreationTestUtils.createQuestDTOs;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DataMongoTest
@ActiveProfiles("test")
class CharacterMongoRepositoryTest {

	private CharacterMongoRepository characterMongoRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@BeforeEach
	void setUp() {
		CharacterMongoDao characterMongoDao = new CharacterMongoDao(mongoTemplate);
		characterMongoRepository = new CharacterMongoRepository(characterMongoDao);

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
		Campaign campaign = createCampaigns().getFirst();
		String campaignSlug = campaign.getSlug();
		String owner = campaign.getOwner();

		// When
		List<Character> actualCharacters = characterMongoRepository.getCharactersByCampaignsSlugAndOwner(campaignSlug, owner);

		// Then
		List<Character> expectedCharacters = List.of(createCharacters().getFirst());

		assertThat(actualCharacters).isEqualTo(expectedCharacters);
	}

	@Test
	@DisplayName("Given an owner, when getting the number of characters created by the user, then the count is returned")
	void givenAnOwner_whenGettingTheNumberOfCharactersCreatedByTheUser_thenTheCountIsReturned() {
		// Given
		String owner = "username";

		// When
		long actualCount = characterMongoRepository.getCountByOwner(owner);

		// Then
		long expectedCount = 3;

		assertThat(actualCount).isEqualTo(expectedCount);
	}

	@Test
	@DisplayName("Given a character, when it does not exist, then it is saved")
	void givenACharacter_whenItDoesNotExist_thenItIsSaved() {
		// Given
		Character character = createCharacter();

		// When & Then
		assertThatCode(() -> characterMongoRepository.save(character)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a character, when save fails because it already exists, then an exception is thrown")
	void givenACharacter_whenSaveFailsBecauseItAlreadyExists_thenAnExceptionIsThrown() {
		// Given
		Character character = createCharacters().getFirst();

		// When & Then
		assertThatCode(() -> characterMongoRepository.save(character)).isInstanceOf(DuplicateException.class);
	}

	@Test
	@DisplayName("Given a character, when save fails, then an exception is thrown")
	void givenACharacter_whenSaveFails_thenAnExceptionIsThrown() {
		// Given & When & Then
		assertThatCode(() -> characterMongoRepository.save(null)).isInstanceOf(InternalException.class);
	}

	@Test
	@DisplayName("Given a character and its old name, when it exists, then it is updated")
	void givenACharacterAndItsOldName_whenItExists_thenItIsUpdated() {
		// Given
		Character character = createCharacters().getFirst();
		Character updatedCharacter = new Character(character.owner(), character.campaignSlug(), "newName");

		// When & Then
		assertThatCode(() -> characterMongoRepository.update(updatedCharacter, character.name())).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a character and its old name, when update fails because a character with the same name already exists, then an exception is thrown")
	void givenACharacterAndItsOldName_whenUpdateFailsBecauseACharacterWithTheSameNameAlreadyExists_thenAnExceptionIsThrown() {
		// Given
		Character character = createCharacter();
		Character characterToUpdate = new Character(
			character.owner(),
			character.campaignSlug(),
			"oldName"
		);

		mongoTemplate.insert(characterToUpdate, "Character");
		mongoTemplate.insert(character, "Character");

		String oldName = characterToUpdate.name();

		// When & Then
		assertThatCode(() -> characterMongoRepository.update(character, oldName)).isInstanceOf(DuplicateException.class);
	}

	@Test
	@DisplayName("Given a character and its old name, when update fails because it is not found, then an exception is thrown")
	void givenACharacterAndItsOldName_whenUpdateFailsBecauseItIsNotFound_thenAnExceptionIsThrown() {
		// Given
		Character character = createCharacter();
		String oldName = character.name();

		// When & Then
		assertThatCode(() -> characterMongoRepository.update(character, oldName)).isInstanceOf(NotFoundException.class);
	}

	@Test
	@DisplayName("Given a character and its old name, when update fails, then an exception is thrown")
	void givenACharacterAndItsOldName_whenUpdateFails_thenAnExceptionIsThrown() {
		// Given & When & Then
		assertThatCode(() -> characterMongoRepository.update(null, null))
			.isInstanceOf(InternalException.class);
	}

	@Test
	@DisplayName("Should delete all characters related to campaignSlug and owner")
	void shouldDeleteAllCharactersRelatedToCampaignSlugAndOwner() {
		// Given
		QuestDTO questDTO = createQuestDTOs().getFirst();
		String campaignSlug = questDTO.getCampaignSlug();
		String owner = questDTO.getOwner();

		// When & Then
		assertThatCode(() -> characterMongoRepository.deleteAllByCampaignSlugAndOwner(campaignSlug, owner)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a character, when it exists, then it is deleted")
	void givenACharacter_whenItExists_thenItIsDeleted() {
		// Given
		Character character = createCharacters().getFirst();

		// When & Then
		assertThatCode(() -> characterMongoRepository.delete(character)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a character, when delete fails because it is not found, then an exception is thrown")
	void givenACharacter_whenDeleteFailsBecauseItIsNotFound_thenAnExceptionIsThrown() {
		// Given
		Character character = new Character("wrong owner", "wrong slug", "wrong name");

		// When & Then
		assertThatCode(() -> characterMongoRepository.delete(character)).isInstanceOf(NotFoundException.class);
	}

	@Test
	@DisplayName("Given a character, when delete fails, then an exception is thrown")
	void givenAWrongCharacter_whenDeleteFails_thenAnExceptionIsThrown() {
		// Given & When & Then
		assertThatCode(() -> characterMongoRepository.delete(null)).isInstanceOf(InternalException.class);
	}

}