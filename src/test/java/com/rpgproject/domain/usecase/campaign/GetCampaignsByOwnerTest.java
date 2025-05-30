package com.rpgproject.domain.usecase.campaign;

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

import static com.rpgproject.domain.EntityCreationTestUtils.createCampaigns;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCampaignsByOwnerTest {

	private GetCampaignsByOwner<?> getCampaignsByOwner;

	@Mock
	private CampaignRepository campaignRepository;

	@Mock
	private Presenter<List<Campaign>, ?> campaignsPresenter;

	@BeforeEach
	void setUp() {
		getCampaignsByOwner = new GetCampaignsByOwner<>(campaignRepository, campaignsPresenter);
	}

	@Test
	@DisplayName("Given an owner, when getting the user's campaigns, then all of its campaigns are presented")
	void givenAnOwner_whenGettingAllTheUsersCampaigns_thenAllOfItsCampaignsArePresented() {
		// Given
		String owner = "owner";
		List<Campaign> campaigns = createCampaigns();

		when(campaignRepository.getCampaignsByOwner(owner)).thenReturn(campaigns);

		// When
		getCampaignsByOwner.execute(owner);

		// Then
		verify(campaignRepository).getCampaignsByOwner(owner);
		verify(campaignsPresenter).ok(campaigns);
	}

}