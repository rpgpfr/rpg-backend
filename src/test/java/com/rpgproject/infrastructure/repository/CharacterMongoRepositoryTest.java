package com.rpgproject.infrastructure.repository;

import com.rpgproject.infrastructure.dao.CharacterMongoDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import static com.rpgproject.infrastructure.DTOCreationTestUtils.createCharacterDTOs;
import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ActiveProfiles("test")
class CharacterMongoRepositoryTest {

	private CharacterMongoRepository characterMongoRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@BeforeEach
	void setUp() {
		CharacterMongoDao mapMongoDao = new CharacterMongoDao(mongoTemplate);
		characterMongoRepository = new CharacterMongoRepository(mapMongoDao);

		mongoTemplate.insert(createCharacterDTOs(), "Character");
	}

	@AfterEach
	void tearDown() {
		mongoTemplate.dropCollection("Character");
	}

	@Test
	@DisplayName("Given an owner, when looking for the number of characters created by the user, then the count is returned")
	void givenAnOwner_whenGettingTheNumberOfCharactersCreatedByTheUser_thenTheCountIsReturned() {
		// Given
		String owner = "username";

		// When
		long actualCount = characterMongoRepository.getCountByOwner(owner);

		// Then
		long expectedCount = 3;

		assertThat(actualCount).isEqualTo(expectedCount);
	}

}