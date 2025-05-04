package com.rpgproject.infrastructure.dao;

import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.rpgproject.infrastructure.dto.CampaignDTO;
import com.rpgproject.infrastructure.exception.campaignmongo.CampaignNotFoundException;
import com.rpgproject.infrastructure.exception.campaignmongo.DuplicateCampaignNameException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static com.rpgproject.infrastructure.DTOCreationTestUtils.createCampaignDTO;
import static com.rpgproject.infrastructure.DTOCreationTestUtils.createCampaignDTOs;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DataMongoTest
@ActiveProfiles("test")
class CampaignMongoDaoTest {

	private CampaignMongoDao campaignMongoDao;

	@Autowired
	private MongoTemplate mongoTemplate;

	@BeforeEach
	void setUp() {
		campaignMongoDao = new CampaignMongoDao(mongoTemplate);
		initializeMongoDB();
	}

	private void initializeMongoDB() {
		IndexOptions indexOptions = new IndexOptions().unique(true);

		mongoTemplate.createCollection("Campaign")
			.createIndex(
				Indexes.compoundIndex(
					Indexes.ascending("slug"),
					Indexes.ascending("owner")
				),
				indexOptions
			);

		mongoTemplate.insert(createCampaignDTOs(), "Campaign");
	}

	@AfterEach
	void tearDown() {
		mongoTemplate.dropCollection("Campaign");
	}

	@Test
	@DisplayName("Given an owner, when getting the user's campaigns, then all of its campaigns are returned")
	void givenAnOwner_whenGettingTheUsersCampaigns_thenAllOfItsCampaignsAreReturned() {
		// Given
		String owner = "username";

		// When
		List<CampaignDTO> actualCampaigns = campaignMongoDao.findAllCampaignsByOwner(owner);

		// Then
		List<CampaignDTO> expectedFullCampaigns = createCampaignDTOs();
		List<CampaignDTO> expectedCampaigns = expectedFullCampaigns
			.stream()
			.map(campaignDTO ->
				new CampaignDTO(
					null,
					campaignDTO.getName(),
					campaignDTO.getSlug(),
					null,
					null,
					null,
					campaignDTO.getCreatedAt()
				)
			)
			.toList();

		assertThat(actualCampaigns).isEqualTo(expectedCampaigns);
	}

	@Test
	@DisplayName("Given a slug and an owner, when the campaign exists, then it is returned")
	void givenASlugAndAnOwner_whenTheCampaignExists_thenItIsReturned() {
		// Given
		CampaignDTO campaignDTO = createCampaignDTOs().getFirst();
		String slug = campaignDTO.getSlug();
		String owner = campaignDTO.getOwner();

		// When
		CampaignDTO actualCampaignDTO = campaignMongoDao.findCampaignBySlugAndOwner(slug, owner);

		// Then
		CampaignDTO expectedCampaignDTO = createCampaignDTOs().getFirst();

		assertThat(actualCampaignDTO).isEqualTo(expectedCampaignDTO);
	}

	@Test
	@DisplayName("Given a slug and an owner, when the campaign is not found, then an exception is thrown")
	void givenASlugAndAnOwner_whenTheCampaignIsNotFound_thenAnExceptionIsThrown() {
		// Given
		String slug = "wrong slug";
		String owner = "wrong owner";

		// When & Then
		assertThatCode(() -> campaignMongoDao.findCampaignBySlugAndOwner(slug, owner)).isInstanceOf(CampaignNotFoundException.class);
	}

	@Test
	@DisplayName("Given a slug and an owner, when the campaign exists, then the id is returned")
	void givenASlugAndAnOwner_whenTheCampaignExists_thenTheIdIsReturned() {
		// Given
		CampaignDTO campaignDTO = createCampaignDTOs().getFirst();
		String slug = campaignDTO.getSlug();
		String owner = campaignDTO.getOwner();

		// When
		String actualId = campaignMongoDao.findCampaignIdBySlugAndOwner(slug, owner);

		// Then
		assertThat(actualId).isNotNull();
	}

	@Test
	@DisplayName("Given a campaignDTO, when it is not found, then an exception is thrown")
	void givenACampaignDTO_whenItIsNotFound_thenAnExceptionIsThrown() {
		// Given & When & Then
		assertThatCode(() -> campaignMongoDao.findCampaignIdBySlugAndOwner(null, null)).isInstanceOf(CampaignNotFoundException.class);
	}

	@Test
	@DisplayName("Given an owner, when getting the number of campaigns created by the user, then the count is returned")
	void givenAnOwner_whenGettingTheNumberOfCampaignsCreatedByTheUser_thenTheCountIsReturned() {
		// Given
		String owner = "username";

		// When
		long actualCount = campaignMongoDao.getCountByOwner(owner);

		// Then
		long expectedCount = 3;

		assertThat(actualCount).isEqualTo(expectedCount);
	}

	@Test
	@DisplayName("Given a campaignDTO, when it does not exist, then it is saved")
	void givenACampaignDTO_whenItDoesNotExist_thenItIsSaved() {
		// Given
		CampaignDTO campaignDTO = createCampaignDTO();

		// When & Then
		assertThatCode(() -> campaignMongoDao.save(campaignDTO)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a campaignDTO, when save fails because it already exists, then an exception is thrown")
	void givenACampaignDTO_whenSaveFailsBecauseItAlreadyExist_thenAnExceptionIsThrown() {
		// Given
		CampaignDTO campaignDTO = createCampaignDTOs().getFirst();

		// When & Then
		assertThatCode(() -> campaignMongoDao.save(campaignDTO)).isInstanceOf(DuplicateCampaignNameException.class);
	}

	@Test
	@DisplayName("Given a campaignDTO, when save fails, then an exception is thrown")
	void givenACampaignDTO_whenSaveFails_thenAnExceptionIsThrown() {
		// Given & When & Then
		assertThatCode(() -> campaignMongoDao.save(null))
			.isInstanceOf(RuntimeException.class)
			.hasMessage("Une erreur est survenue lors de la création de la campagne.");
	}

	@Test
	@DisplayName("Given a campaignDTO, when it exists, then it is updated")
	void givenACampaignDTO_whenItExists_thenItIsUpdated() {
		// Given
		CampaignDTO campaignDTO = createCampaignDTOs().getFirst();
		String slug = campaignDTO.getSlug();

		campaignDTO.setName("updated");

		// When & Then
		assertThatCode(() -> campaignMongoDao.update(campaignDTO, slug)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a campaignDTO, when update fails because it is not found, then an exception is thrown")
	void givenACampaignDTOWithWrongOwner_whenUpdateFailsBecauseItIsNotFound_thenAnExceptionIsThrown() {
		// Given
		CampaignDTO campaignDTO = createCampaignDTO();
		String slug = campaignDTO.getSlug();

		campaignDTO.setOwner("wrong owner");

		// When & Then
		assertThatCode(() -> campaignMongoDao.update(campaignDTO, slug)).isInstanceOf(CampaignNotFoundException.class);
	}

	@Test
	@DisplayName("Given a campaignDTO with wrong slug, when update fails because it is not found, then an exception is thrown")
	void givenACampaignDTOWithWrongSlug_whenUpdateFailsBecauseItIsNotFound_thenAnExceptionIsThrown() {
		// Given
		CampaignDTO campaignDTO = createCampaignDTO();
		String slug = "wrong slug";

		// When & Then
		assertThatCode(() -> campaignMongoDao.update(campaignDTO, slug)).isInstanceOf(CampaignNotFoundException.class);
	}

	@Test
	@DisplayName("Given a campaignDTO, when update fails, then an exception is thrown")
	void givenACampaignDTO_whenUpdateFails_thenAnExceptionIsThrown() {
		// Given & When & Then
		assertThatCode(() -> campaignMongoDao.update(null, null))
			.isInstanceOf(RuntimeException.class)
			.hasMessage("Une erreur est survenue lors de la mise à jour des informations.");
	}

	@Test
	@DisplayName("Given a campaignDTO, when it exists, then it is deleted")
	void givenACampaignDTO_whenItExists_thenItIsDeleted() {
		// Given
		CampaignDTO campaignDTO = createCampaignDTOs().getFirst();

		// When & Then
		assertThatCode(() -> campaignMongoDao.delete(campaignDTO)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a campaignDTO, when delete fails because it is not found, then an exception is thrown")
	void givenACampaignDTO_whenDeleteFailsBecauseItIsNotFound_thenAnExceptionIsThrown() {
		// Given
		CampaignDTO campaignDTO = new CampaignDTO("wrong owner", "wrong name", "wrong slug", "description", "type", "mood", LocalDate.of(2025, 1, 1));
		campaignDTO.setId("wrong id");

		// When & Then
		assertThatCode(() -> campaignMongoDao.delete(campaignDTO)).isInstanceOf(CampaignNotFoundException.class);
	}

	@Test
	@DisplayName("Given a campaignDTO, when delete fails, then an exception is thrown")
	void givenAWrongCampaignDTO_whenDeleteFails_thenAnExceptionIsThrown() {
		// Given & When & Then
		assertThatCode(() -> campaignMongoDao.delete(null))
			.isInstanceOf(RuntimeException.class)
			.hasMessage("Une erreur est survenue lors de la suppression de la campagne.");
	}

}