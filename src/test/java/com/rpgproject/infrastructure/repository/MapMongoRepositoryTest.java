package com.rpgproject.infrastructure.repository;

import com.rpgproject.infrastructure.dao.MapMongoDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import static com.rpgproject.infrastructure.DTOCreationTestUtils.createMapDTOs;
import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ActiveProfiles("test")
class MapMongoRepositoryTest {

	private MapMongoRepository mapMongoRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@BeforeEach
	void setUp() {
		MapMongoDao mapMongoDao = new MapMongoDao(mongoTemplate);
		mapMongoRepository = new MapMongoRepository(mapMongoDao);

		mongoTemplate.insert(createMapDTOs(), "Map");
	}

	@AfterEach
	void tearDown() {
		mongoTemplate.dropCollection("Map");
	}

	@Test
	@DisplayName("Given an owner, when looking for the number of maps created by the user, then the count is returned")
	void givenAnOwner_whenGettingTheNumberOfMapsCreatedByTheUser_thenTheCountIsReturned() {
		// Given
		String owner = "username";

		// When
		long actualCount = mapMongoRepository.getCountByOwner(owner);

		// Then
		long expectedCount = 3;

		assertThat(actualCount).isEqualTo(expectedCount);
	}

}