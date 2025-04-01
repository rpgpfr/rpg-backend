package com.rpgproject.infrastructure.repository;

import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.exception.campaign.CampaignCreationFailedException;
import com.rpgproject.domain.exception.campaign.CampaignNotFoundException;
import com.rpgproject.domain.exception.campaign.CampaignUpdateFailedException;
import com.rpgproject.infrastructure.dao.CampaignMongoDao;
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

import static com.rpgproject.domain.EntityCreationTestUtils.createCampaigns;
import static com.rpgproject.infrastructure.DTOCreationTestUtils.createCampaignDTOs;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DataMongoTest
@ActiveProfiles("test")
class CampaignMongoRepositoryTest {

	private CampaignMongoRepository campaignMongoRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@BeforeEach
	public void setUp() {
		CampaignMongoDao campaignMongoDao = new CampaignMongoDao(mongoTemplate);
		campaignMongoRepository = new CampaignMongoRepository(campaignMongoDao);

		mongoTemplate.insert(createCampaignDTOs(), "Campaign");
	}

	@AfterEach
	public void tearDown() {
		mongoTemplate.dropCollection("Campaign");
	}

	@Test
	@DisplayName("Given an owner, when looking for users campaigns, then all of its campaigns are returned")
	void givenAnOwner_whenLookingForUsersCampaigns_thenAllOfItsCampaignsAreReturned() {
		// Given
		String owner = "username";

		// When
		List<Campaign> actualCampaigns = campaignMongoRepository.getCampaignsByOwner(owner);

		// Then
		List<Campaign> expectedCampaigns = createCampaigns();

		assertThat(actualCampaigns).isEqualTo(expectedCampaigns);
	}

	@Test
	@DisplayName("Given a slug and an owner, when getting a campaign, then it is returned")
	void givenASlugAndAnOwner_whenGettingACampaign_thenItIsReturned() {
		// Given
		Campaign campaign = createCampaigns().getFirst();
		String owner = campaign.getOwner();
		String slug = campaign.getSlug();

		// When
		Campaign actualCampaign = campaignMongoRepository.getCampaignBySlugAndOwner(slug, owner);

		// Then
		Campaign expectedCampaign = createCampaigns().getFirst();

		assertThat(actualCampaign).isEqualTo(expectedCampaign);
	}

	@Test
	@DisplayName("Given a slug and an owner, when getting a campain, then an exception is thrown")
	void givenASlugAndAnOwner_whenGettingACampaign_thenAnExceptionIsThrown() {
		// When & Then
		assertThatCode(() -> campaignMongoRepository.getCampaignBySlugAndOwner(null, null)).isInstanceOf(CampaignNotFoundException.class);
	}

	@Test
	@DisplayName("Given an owner, when getting the number of campaigns created by the user, then the count is returned")
	void givenAnOwner_whenGettingTheNumberOfCampaignsCreatedByTheUser_thenTheCountIsReturned() {
		// Given
		String owner = "username";

		// When
		long actualCount = campaignMongoRepository.getCountByOwner(owner);

		// Then
		long expectedCount = 3;

		assertThat(actualCount).isEqualTo(expectedCount);
	}

	@Test
	@DisplayName("Given a campaign, when saving it, then it is saved")
	void givenACampaign_whenSavingTheCampaign_thenCampaignIsSaved() {
		// Given
		Campaign campaign = new Campaign("alvin", "myCampaign", "mycampaign");

		// When
		campaignMongoRepository.save(campaign);

		// Then
		List<Campaign> actualCampaigns = campaignMongoRepository.getCampaignsByOwner("alvin");
		List<Campaign> expectedCampaigns = List.of(new Campaign("alvin", "myCampaign", "mycampaign"));

		assertThat(actualCampaigns).isEqualTo(expectedCampaigns);
	}

	@Test
	@DisplayName("Given a campaign, when saving fails, then an exception is thrown")
	void givenACampaign_whenSavingFails_thenAnExceptionIsThrown() {
		// When & Then
		assertThatCode(() -> campaignMongoRepository.save(null)).isInstanceOf(CampaignCreationFailedException.class);
	}

	@Test
	@DisplayName("Given a campaignDTO, when updating it, then it is updated")
	void givenACampaignDTO_whenUpdatingIt_thenItIsUpdated() {
		// Given
		Campaign oldCampaign = createCampaigns().getFirst();
		Campaign campaign = new Campaign(oldCampaign.getOwner(), "updated name", "updated-name");
		String slug = oldCampaign.getSlug();

		// When
		campaignMongoRepository.update(campaign, slug);

		// Then
		List<Campaign> actualCampaigns = campaignMongoRepository.getCampaignsByOwner("username");

		Campaign expectedCampaign = new Campaign(createCampaignDTOs().getFirst().getOwner(), "updated name", "updated-name");

		assertThat(actualCampaigns).contains(expectedCampaign);
	}

	@Test
	@DisplayName("Given a campaignDTO, when updating fails, then an exception is thrown")
	void givenACampaignDTO_whenUpdatingFails_thenAnExceptionIsThrown() {
		// When & Then
		assertThatCode(() -> campaignMongoRepository.update(null, null)).isInstanceOf(CampaignUpdateFailedException.class);
	}

}