package com.rpgproject.domain.usecase.campaign;

import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.exception.CampaignCreationFailedException;
import com.rpgproject.domain.port.CampaignRepository;
import com.rpgproject.domain.port.Presenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.rpgproject.utils.CreationTestUtils.createCampaign;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCampaignTest {

	private CreateCampaign<?> createCampaign;

	@Mock
	private CampaignRepository campaignRepository;

	@Mock
	private Presenter<Campaign, ?> presenter;

	@BeforeEach
	public void setUp() {
		createCampaign = new CreateCampaign<>(campaignRepository, presenter);
	}

	@Test
	@DisplayName("Given a campaign, when creating it, then it is saved")
	void givenACampaign_whenCreatingIt_thenItIsSaved() {
		// Given
		Campaign campaign = createCampaign();

		// When
		createCampaign.execute(campaign);

		// Then
		verify(campaignRepository).save(campaign);
		verify(presenter, times(1)).ok();
	}

	@Test
	@DisplayName("Given a campaign, when creation fails, then an exception is thrown")
	void givenACampaign_whenCreationFails_thenAnExceptionIsThrown() {
		// Given
		Campaign campaign = createCampaign();
		CampaignCreationFailedException exception = new CampaignCreationFailedException();

		doThrow(exception).when(campaignRepository).save(campaign);

		// When
		createCampaign.execute(campaign);

		// Then
		verify(campaignRepository).save(campaign);
		verify(presenter, times(1)).error(exception);
	}

}