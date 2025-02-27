package com.rpgproject.infrastructure.dao;

import com.rpgproject.config.ApplicationConfig;
import com.rpgproject.infrastructure.dto.CampaignDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;

import static com.rpgproject.utils.CreationTestUtils.createCampaignDTOs;
import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ActiveProfiles("test")
@Import(ApplicationConfig.class)
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
	@DisplayName("Given a userId, when looking for all the user's campaigns, then all of its campaigns are returned")
	void givenAUserId_whenLookingForAllTheUsersCampaigns_thenAllOfItsCampaignsAreReturned() {
		// Given
		String userId = "username";

		// When
		List<CampaignDTO> actualCampaigns = campaignMongoDao.findAllCampaignsByUserId(userId);

		// Then
		List<CampaignDTO> expectedCampaigns = createCampaignDTOs();

		assertThat(actualCampaigns).isEqualTo(expectedCampaigns);
	}

}