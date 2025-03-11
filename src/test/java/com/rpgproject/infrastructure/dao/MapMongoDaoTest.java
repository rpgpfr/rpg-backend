package com.rpgproject.infrastructure.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import static com.rpgproject.utils.CreationTestUtils.createMapDTOs;
import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ActiveProfiles("test")
class MapMongoDaoTest {

	private MapMongoDao mapMongoDao;

	@Autowired
	private MongoTemplate mongoTemplate;

	@BeforeEach
	public void setUp() {
		mapMongoDao = new MapMongoDao(mongoTemplate);

		mongoTemplate.insert(createMapDTOs(), "Map");
	}

	@AfterEach
	public void tearDown() {
		mongoTemplate.dropCollection("Map");
	}

	@Test
	@DisplayName("Given a userId, when looking for the number of maps created by the user, then the count is returned")
	void givenAUserId_whenGettingTheNumberOfMapsCreatedByTheUser_thenTheCountIsReturned() {
		// Given
		String userId = "username";

		// When
		long actualCount = mapMongoDao.getCountByUserId(userId);

		// Then
		long expectedCount = 3;

		assertThat(actualCount).isEqualTo(expectedCount);
	}

}