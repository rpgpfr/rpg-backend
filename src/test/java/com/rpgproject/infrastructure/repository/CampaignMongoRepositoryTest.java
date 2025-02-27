package com.rpgproject.infrastructure.repository;

import com.rpgproject.config.ApplicationConfig;
import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.infrastructure.dao.CampaignMongoDao;
import com.rpgproject.infrastructure.dto.CampaignDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.ExecutableFindOperation;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.rpgproject.utils.CreationTestUtils.createCampaignDTOs;
import static com.rpgproject.utils.CreationTestUtils.createCampaigns;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@DataMongoTest
@ActiveProfiles("test")
@Import(ApplicationConfig.class)
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
	@DisplayName("Given a user id, when looking for users campaigns, then all of its campaigns are returned")
	void givenAUserId_whenLookingForUsersCampaigns_thenAllOfItsCampaignsAreReturned() {
		// Given
		String userId = "username";

		// When
		List<Campaign> actualCampaigns = campaignMongoRepository.getCampaignsByUserId(userId);

		// Then
		List<Campaign> expectedCampaigns = createCampaigns();

		assertThat(actualCampaigns).isEqualTo(expectedCampaigns);
	}

}