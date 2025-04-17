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

import java.time.LocalDate;
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
	@DisplayName("Given an owner, when getting users campaigns, then all of its campaigns are returned")
	void givenAnOwner_whenGettingUsersCampaigns_thenAllOfItsCampaignsAreReturned() {
		// Given
		String owner = "username";

		// When
		List<Campaign> actualCampaigns = campaignMongoRepository.getCampaignsByOwner(owner);

		// Then
		List<Campaign> expectedFullCampaigns = createCampaigns();
		List<Campaign> expectedCampaigns = expectedFullCampaigns
			.stream()
			.map(campaign ->
				new Campaign(
					null,
					campaign.getName(),
					campaign.getSlug(),
					null,
					null,
					null,
					null,
					campaign.getCreatedAt()
				)
			)
			.toList();

		assertThat(actualCampaigns).isEqualTo(expectedCampaigns);
	}

	@Test
	@DisplayName("Given a slug and an owner, when the campaign exists, then it is returned")
	void givenASlugAndAnOwner_whenTheCampaignExists_thenItIsReturned() {
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
	@DisplayName("Given a slug and an owner, when the campaign does not exist, then an exception is thrown")
	void givenASlugAndAnOwner_whenTheCampaignDoesNotExist_thenAnExceptionIsThrown() {
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
		Campaign campaign = new Campaign("alvin", "myCampaign", "mycampaign", null);

		// When & Then
		assertThatCode(() -> campaignMongoRepository.save(campaign)).doesNotThrowAnyException();
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
		Campaign campaign = new Campaign(oldCampaign.getOwner(), "updated name", "updated-name", null);
		String slug = oldCampaign.getSlug();

		// When & Then
		assertThatCode(() -> campaignMongoRepository.update(campaign, slug)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a campaignDTO, when updating fails, then an exception is thrown")
	void givenACampaignDTO_whenUpdatingFails_thenAnExceptionIsThrown() {
		// When & Then
		assertThatCode(() -> campaignMongoRepository.update(null, null)).isInstanceOf(CampaignUpdateFailedException.class);
	}

	@Test
	@DisplayName("Given a campaign, when deleting it, then it is deleted")
	void givenACampaign_whenDeletingTheCampaign_thenCampaignIsDeleted() {
		// Given
		Campaign campaign = createCampaigns().getFirst();
		String slug = campaign.getSlug();
		String owner = campaign.getOwner();

		// When & Then
		assertThatCode(() -> campaignMongoRepository.delete(slug, owner)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a campaign, when deleting fails, then an exception is thrown")
	void givenACampaign_whenDeletingFails_thenAnExceptionIsThrown() {
		// When & Then
		assertThatCode(() -> campaignMongoRepository.delete(null, null)).isInstanceOf(CampaignNotFoundException.class);
	}

}