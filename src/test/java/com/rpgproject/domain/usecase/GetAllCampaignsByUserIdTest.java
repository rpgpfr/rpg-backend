package com.rpgproject.domain.usecase;

import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.port.CampaignRepository;
import com.rpgproject.domain.port.Presenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.rpgproject.utils.CreationTestUtils.createCampaigns;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllCampaignsByUserIdTest {

	private GetAllCampaignsByUserId<?> getAllCampaignsByUserId;

	@Mock
	private CampaignRepository campaignRepository;

	@Mock
	private Presenter<List<Campaign>, ?> campaignsPresenter;

	@BeforeEach
	public void setUp() {
		getAllCampaignsByUserId = new GetAllCampaignsByUserId<>(campaignRepository, campaignsPresenter);
	}

	@Test
	@DisplayName("Given a userId, when getting all the users campaigns Then all of its campaigns are presented")
	void givenAUserId_whenGettingAllTheUsersCampaigns_thenAllOfItsCampaignsArePresented() {
		// Given
		String userId = "userId";
		List<Campaign> campaigns = createCampaigns();

		when(campaignRepository.getCampaignsByUserId(userId)).thenReturn(campaigns);

		// When
		getAllCampaignsByUserId.execute(userId);

		// Then
		verify(campaignRepository).getCampaignsByUserId(userId);
		verify(campaignsPresenter).ok(campaigns);
	}

}