package com.rpgproject.infrastructure.dao;

import com.rpgproject.infrastructure.dto.CampaignDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.ExecutableFindOperation;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

import static com.rpgproject.utils.CreationTestUtils.createCampaignDTOs;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@ExtendWith(MockitoExtension.class)
class CampaignMongoDaoTest {

	private CampaignMongoDao campaignMongoDao;

	@Mock
	private MongoTemplate mongoTemplate;

	@BeforeEach
	public void setup() {
		campaignMongoDao = new CampaignMongoDao(mongoTemplate);
	}

	@Test
	@DisplayName("Given a userId, when looking for all the user's campaigns, then all of its campaigns are returned")
	void givenAUserId_whenLookingForAllTheUsersCampaigns_thenAllOfItsCampaignsAreReturned() {
		// Given
		String userId = "username";
		ExecutableFindOperation.ExecutableFind<CampaignDTO> executableFind = mock(ExecutableFindOperation.ExecutableFind.class);
		ExecutableFindOperation.TerminatingFind<CampaignDTO> terminatingFind = mock(ExecutableFindOperation.TerminatingFind.class);

		when(mongoTemplate.query(CampaignDTO.class)).thenReturn(executableFind);
		when(executableFind.matching(query(where("userId").is(userId)))).thenReturn(terminatingFind);
		when(terminatingFind.all()).thenReturn(createCampaignDTOs());

		// When
		List<CampaignDTO> actualCampaigns = campaignMongoDao.findAllCampaignsByUserId(userId);

		// Then
		List<CampaignDTO> expectedCampaigns = createCampaignDTOs();

		assertThat(actualCampaigns).isEqualTo(expectedCampaigns);
	}

}