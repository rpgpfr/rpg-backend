package com.rpgproject.domain.usecase.campaign;

import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.exception.InternalException;
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
	void setUp() {
		updateCampaign = new UpdateCampaign<>(campaignRepository, presenter);
	}

	@Test
	@DisplayName("Given a campaign and its slug, when updating it, then a success is presented")
	void givenACampaignAndItsSlug_whenUpdatingIt_thenASuccessIsPresented() {
		// Given
		Campaign campaign = new Campaign("alvin", "updated name", "updated-name", null);
		String slug = "camapgne-1";

		// When
		updateCampaign.execute(campaign, slug);

		// Then
		verify(campaignRepository).update(campaign, slug);
		verify(presenter, times(1)).ok();
	}

	@Test
	@DisplayName("Given a campaign and its slug, when update fails, then an exception is presented")
	void givenACampaignAndItsSlug_whenUpdateFails_thenAnExceptionIsPresented() {
		// Given
		Campaign campaign = new Campaign("alvin", "updated name", "updated-name", null);
		String slug = "slug";
		InternalException exception = new InternalException("error");

		doThrow(exception).when(campaignRepository).update(campaign, slug);

		// When
		updateCampaign.execute(campaign, slug);

		// Then
		verify(campaignRepository).update(campaign, slug);
		verify(presenter, times(1)).error(exception);
	}

}