package com.rpgproject.domain.usecase.campaign;

import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.entity.Quest;
import com.rpgproject.domain.exception.campaign.CampaignCreationFailedException;
import com.rpgproject.domain.exception.campaign.CampaignNotFoundException;
import com.rpgproject.domain.port.CampaignRepository;
import com.rpgproject.domain.port.Presenter;
import com.rpgproject.domain.port.QuestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.rpgproject.domain.EntityCreationTestUtils.createCampaign;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteCampaignTest {

	private DeleteCampaign<?> deleteCampaign;

	@Mock
	private CampaignRepository campaignRepository;

	@Mock
	private Presenter<Campaign, ?> presenter;

	@BeforeEach
	public void setUp() {
		deleteCampaign = new DeleteCampaign<>(campaignRepository, presenter);
	}

	@Test
	@DisplayName("Given a campaign, when creating it, then it is saved")
	void givenACampaign_whenCreatingIt_thenItIsSaved() {
		// Given
		Campaign campaign = createCampaign();

		// When
		deleteCampaign.execute(campaign);

		// Then

		verify(campaignRepository, times(1)).delete(campaign);
		verify(presenter, times(1)).ok();
	}

	@Test
	@DisplayName("Given a campaign, when creation fails, then an exception is thrown")
	void givenACampaign_whenCreationFails_thenAnExceptionIsThrown() {
		// Given
		Campaign campaign = createCampaign();
		CampaignNotFoundException exception = new CampaignNotFoundException();

		doThrow(exception).when(campaignRepository).delete(campaign);

		// When
		deleteCampaign.execute(campaign);

		// Then

		verify(campaignRepository).delete(campaign);
		verify(presenter, times(1)).error(exception);
	}

}