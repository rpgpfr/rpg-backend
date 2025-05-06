package com.rpgproject.infrastructure.repository;

import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.exception.DuplicateException;
import com.rpgproject.domain.exception.InternalException;
import com.rpgproject.domain.exception.NotFoundException;
import com.rpgproject.infrastructure.dao.CampaignMongoDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static com.rpgproject.domain.EntityCreationTestUtils.createCampaign;
import static com.rpgproject.domain.EntityCreationTestUtils.createCampaigns;
import static com.rpgproject.infrastructure.DTOCreationTestUtils.createCampaignDTOs;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

@DataMongoTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class CampaignMongoRepositoryTest {

	private CampaignMongoRepository campaignMongoRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@BeforeEach
	void setUp() {
		CampaignMongoDao campaignMongoDao = new CampaignMongoDao(mongoTemplate);
		campaignMongoRepository = new CampaignMongoRepository(campaignMongoDao);
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
	@DisplayName("Given an owner, when getting user's campaigns, then all of its campaigns are returned")
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
	@DisplayName("Given a slug and an owner, when the campaign is not found, then an exception is thrown")
	void givenASlugAndAnOwner_whenTheCampaignIsNotFound_thenAnExceptionIsThrown() {
		// Given & When & Then
		assertThatCode(() -> campaignMongoRepository.getCampaignBySlugAndOwner(null, null)).isInstanceOf(NotFoundException.class);
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
	@DisplayName("Given a campaign, when it does not exist, then it is saved")
	void givenACampaign_whenItDoesNotExist_thenItIsSaved() {
		// Given
		Campaign campaign = new Campaign("alvin", "myCampaign", "mycampaign", null);

		// When & Then
		assertThatCode(() -> campaignMongoRepository.save(campaign)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a campaign, when save fails because it already exists, then an exception is thrown")
	void givenACampaign_whenSaveFailsBecauseItAlreadyExists_thenAnExceptionIsThrown() {
		// Given & When & Then
		Campaign campaign = createCampaigns().getFirst();

		assertThatCode(() -> campaignMongoRepository.save(campaign)).isInstanceOf(DuplicateException.class);
	}

	@Test
	@DisplayName("Given a campaign, when save fails, then an exception is thrown")
	void givenACampaign_whenSaveFails_thenAnExceptionIsThrown() {
		// Given & When & Then
		assertThatCode(() -> campaignMongoRepository.save(null)).isInstanceOf(InternalException.class);
	}

	@Test
	@DisplayName("Given a campaign, when it exists, then it is updated")
	void givenACampaign_whenItExists_thenItIsUpdated() {
		// Given
		Campaign oldCampaign = createCampaigns().getFirst();
		Campaign campaign = new Campaign(oldCampaign.getOwner(), "updated name", "updated-name", null);
		String slug = oldCampaign.getSlug();

		// When & Then
		assertThatCode(() -> campaignMongoRepository.update(campaign, slug)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a campaign, when update fails because it is not found, then an exception is thrown")
	void givenACampaign_whenUpdateFailsBecauseItIsNotFound_thenAnExceptionIsThrown() {
		// Given
		String slug = "wrong slug";
		Campaign campaign = createCampaign();

		// When & Then
		assertThatCode(() -> campaignMongoRepository.update(campaign, slug)).isInstanceOf(NotFoundException.class);
	}

	@Test
	@DisplayName("Given a campaign, when update fails, then an exception is thrown")
	void givenACampaign_whenUpdateFails_thenAnExceptionIsThrown() {
		// Given & When & Then
		assertThatCode(() -> campaignMongoRepository.update(null, null)).isInstanceOf(InternalException.class);
	}

	@Test
	@DisplayName("Given a campaign, when it exists, then it is deleted")
	void givenACampaign_whenItExists_thenItIsDeleted() {
		// Given
		Campaign campaign = createCampaigns().getFirst();
		String slug = campaign.getSlug();
		String owner = campaign.getOwner();

		// When & Then
		assertThatCode(() -> campaignMongoRepository.delete(slug, owner)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a campaign, when delete fails because it is not found, then an exception is thrown")
	void givenACampaign_whenDeleteFailsBecauseItIsNotFound_thenAnExceptionIsThrown() {
		// Given & When & Then
		assertThatCode(() -> campaignMongoRepository.delete(null, null)).isInstanceOf(NotFoundException.class);
	}

	@Test
	@DisplayName("Given campaign, when delete fails, then an exception is thrown")
	void givenACampaign_whenDeleteFails_thenAnExceptionIsThrown() {
		// Given
		CampaignMongoDao campaignMongoDaoMock = mock(CampaignMongoDao.class);

		ReflectionTestUtils.setField(campaignMongoRepository, "campaignMongoDao", campaignMongoDaoMock);

		doThrow(new RuntimeException()).when(campaignMongoDaoMock).delete(null);

		// When & Then
		assertThatCode(() -> campaignMongoRepository.delete(null, null)).isInstanceOf(InternalException.class);
	}

}