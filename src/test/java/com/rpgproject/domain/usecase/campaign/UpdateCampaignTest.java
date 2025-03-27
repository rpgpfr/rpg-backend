package com.rpgproject.domain.usecase.campaign;

import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.exception.CampaignUpdateFailedException;
import com.rpgproject.domain.port.CampaignRepository;
import com.rpgproject.domain.port.Presenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCampaignTest {

	private UpdateCampaign<?> updateCampaign;

	@Mock
	private CampaignRepository campaignRepository;

	@Mock
	private Presenter<Campaign, ?> presenter;

	@BeforeEach
	public void setUp() {
		updateCampaign = new UpdateCampaign<>(campaignRepository, presenter);
	}

	@Test
	@DisplayName("Given a campaign and its original name, when updating it, then it is updated")
	void givenACampaignAndItsOriginalName_whenUpdatingIt_thenItIsUpdated() {
		// Given
		Campaign campaign = new Campaign("alvin", "updated name", "updated-name");
		String originalName = "originalName";

		// When
		updateCampaign.execute(campaign, originalName);

		// Then
		verify(campaignRepository).update(campaign, originalName);
		verify(presenter, times(1)).ok(campaign);
	}

	@Test
	@DisplayName("Given a campaign and its original name, when update fails, then an error is presented")
	void givenACampaignAndItsOriginalName_whenUpdateFails_thenAnErrorIsPresented() {
		// Given
		Campaign campaign = new Campaign("alvin", "updated name", "updated-name");
		String originalName = "originalName";
		CampaignUpdateFailedException exception = new CampaignUpdateFailedException();

		doThrow(exception).when(campaignRepository).update(campaign, originalName);

		// When
		updateCampaign.execute(campaign, originalName);

		// Then
		verify(campaignRepository).update(campaign, originalName);
		verify(presenter, times(1)).error(exception);
	}

}