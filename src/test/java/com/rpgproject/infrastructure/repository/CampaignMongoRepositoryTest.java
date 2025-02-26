package com.rpgproject.infrastructure.repository;

import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.infrastructure.dao.CampaignMongoDao;
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
import static com.rpgproject.utils.CreationTestUtils.createCampaigns;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@ExtendWith(MockitoExtension.class)
class CampaignMongoRepositoryTest {

	private CampaignMongoRepository campaignMongoRepository;
	private CampaignMongoDao campaignMongoDao;

	@Mock
	private MongoTemplate mongoTemplate;

	@BeforeEach
	public void setUp() {
		campaignMongoDao = new CampaignMongoDao(mongoTemplate);
		campaignMongoRepository = new CampaignMongoRepository(campaignMongoDao);
	}

	@Test
	@DisplayName("Given a user id, when looking for users campaigns, then all of its campaigns are returned")
	void givenAUserId_whenLookingForUsersCampaigns_thenAllOfItsCampaignsAreReturned() {
		// Given
		String userId = "username";
		List<CampaignDTO> campaignDTOs = createCampaignDTOs();

		ExecutableFindOperation.ExecutableFind<CampaignDTO> executableFind = mock(ExecutableFindOperation.ExecutableFind.class);
		ExecutableFindOperation.TerminatingFind<CampaignDTO> terminatingFind = mock(ExecutableFindOperation.TerminatingFind.class);

		when(mongoTemplate.query(CampaignDTO.class)).thenReturn(executableFind);
		when(executableFind.matching(query(where("userId").is(userId)))).thenReturn(terminatingFind);
		when(terminatingFind.all()).thenReturn(campaignDTOs);

		// When
		List<Campaign> actualCampaigns = campaignMongoRepository.getCampaignsByUserId(userId);

		// Then
		List<Campaign> expectedCampaigns = createCampaigns();

		assertThat(actualCampaigns).isEqualTo(expectedCampaigns);
	}

}