package com.rpgproject.infrastructure.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import static com.rpgproject.utils.CreationTestUtils.createCharacterDTOs;
import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ActiveProfiles("test")
class CharacterMongoDaoTest {

	private CharacterMongoDao characterMongoDao;

	@Autowired
	private MongoTemplate mongoTemplate;

	@BeforeEach
	public void setUp() {
		characterMongoDao = new CharacterMongoDao(mongoTemplate);

		mongoTemplate.insert(createCharacterDTOs(), "Character");
	}

	@AfterEach
	public void tearDown() {
		mongoTemplate.dropCollection("Character");
	}

	@Test
	@DisplayName("Given a userId, when getting the number of characters created by the user, then the count is returned")
	void givenAUserId_whenGettingTheNumberOfCharactersCreatedByTheUser_thenTheCountIsReturned() {
		// Given
		String userId = "username";

		// When
		long actualCount = characterMongoDao.getCountByOwner(userId);

		// Then
		long expectedCount = 3;

		assertThat(actualCount).isEqualTo(expectedCount);
	}

}