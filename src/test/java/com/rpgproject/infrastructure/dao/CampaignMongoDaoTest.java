package com.rpgproject.infrastructure.dao;

import com.rpgproject.infrastructure.dto.CampaignDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

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
	public void setUp() {
		campaignMongoDao = new CampaignMongoDao(mongoTemplate);
		mongoTemplate.insert(createCampaignDTOs(), "Campaign");
	}

	@AfterEach
	public void tearDown() {
		mongoTemplate.dropCollection("Campaign");
	}

	@Test
	@DisplayName("Given an owner, when looking for all the user's campaigns, then all of its campaigns are returned")
	void givenAnOwner_whenLookingForAllTheUsersCampaigns_thenAllOfItsCampaignsAreReturned() {
		// Given
		String owner = "username";

		// When
		List<CampaignDTO> actualCampaigns = campaignMongoDao.findAllCampaignsByOwner(owner);

		// Then
		List<CampaignDTO> expectedCampaigns = createCampaignDTOs();

		assertThat(actualCampaigns).isEqualTo(expectedCampaigns);
	}

	@Test
	@DisplayName("Given a slug and an owner, when getting the campaign, then it is returned")
	void givenASlugAndAnOwner_whenGettingTheCampaign_thenItIsReturned() {
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
	@DisplayName("Given a slug and an owner, when getting the campaign id, then the id is returned")
	void givenASlugAndAnOwner_whenGettingTheCampaignId_thenTheIdIsReturned() {
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
	@DisplayName("Given a campaignDTO, when getting the campaign id, then an exception is thrown")
	void givenACampaignDTO_whenGettingTheCampaignId_thenAnExceptionIsThrown() {
		// Given & When & Then
		assertThatCode(() -> campaignMongoDao.findCampaignIdBySlugAndOwner(null, null)).isInstanceOf(RuntimeException.class);
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
	@DisplayName("Given a campaignDTO, when saving it, then it is saved")
	void givenACampaignDTO_whenSavingIt_thenItIsSaved() {
		// Given
		CampaignDTO campaignDTO = createCampaignDTO();

		// When & Then
		assertThatCode(() -> campaignMongoDao.save(campaignDTO)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a campaignDTO, when saving fails, then an exception is thrown")
	void givenACampaignDTO_whenSavingFails_thenAnExceptionIsThrown() {
		// Given & When & Then
		assertThatCode(() -> campaignMongoDao.save(null)).isInstanceOf(RuntimeException.class);
	}

	@Test
	@DisplayName("Given a campaignDTO, when updating it, then it is updated")
	void givenACampaignDTO_whenUpdatingIt_thenItIsUpdated() {
		// Given
		CampaignDTO campaignDTO = createCampaignDTOs().getFirst();
		String slug = campaignDTO.getSlug();

		campaignDTO.setName("updated");

		// When & Then
		assertThatCode(() -> campaignMongoDao.update(campaignDTO, slug)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a campaignDTO with wrong owner, when updating, then an exception is thrown")
	void givenACampaignDTOWithWrongOwner_whenUpdatingFails_thenAnExceptionIsThrown() {
		// Given
		CampaignDTO campaignDTO = createCampaignDTO();
		String slug = campaignDTO.getSlug();

		campaignDTO.setOwner("wrong owner");

		// When & Then
		assertThatCode(() -> campaignMongoDao.update(campaignDTO, slug)).isInstanceOf(RuntimeException.class);
	}

	@Test
	@DisplayName("Given a campaignDTO with wrong slug, when updating, then an exception is thrown")
	void givenACampaignDTOWithWrongSlug_whenUpdatingFails_thenAnExceptionIsThrown() {
		// Given
		CampaignDTO campaignDTO = createCampaignDTO();
		String slug = "wrong slug";

		// When & Then
		assertThatCode(() -> campaignMongoDao.update(campaignDTO, slug)).isInstanceOf(RuntimeException.class);
	}

	@Test
	@DisplayName("Given a campaignDTO, when updating fails, then an exception is thrown")
	void givenACampaignDTO_whenUpdatingFails_thenAnExceptionIsThrown() {
		// Given & When & Then
		assertThatCode(() -> campaignMongoDao.update(null, null)).isInstanceOf(RuntimeException.class);
	}

	@Test
	@DisplayName("Given a campaignDTO, when deleting it, then it is deleted")
	void givenACampaignDTO_whenDeletingIt_thenItIsDeleted() {
		// Given
		CampaignDTO campaignDTO = createCampaignDTOs().getFirst();

		// When & Then
		assertThatCode(() -> campaignMongoDao.delete(campaignDTO)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a campaignDTO, when deleting it, then it is deleted")
	void givenAWrongCampaignDTO_whenDeletingIt_thenAnExceptionIsThrown() {
		// Given
		CampaignDTO campaignDTO = new CampaignDTO("wrong owner", "wrong name", "wrong slug", "description", "type", "mood");
		campaignDTO.setId("wrong id");

		// When & Then
		assertThatCode(() -> campaignMongoDao.delete(campaignDTO)).isInstanceOf(RuntimeException.class);
	}

}