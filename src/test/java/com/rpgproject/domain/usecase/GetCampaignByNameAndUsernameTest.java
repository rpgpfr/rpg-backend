package com.rpgproject.domain.usecase;

import com.rpgproject.domain.bean.Campaign;
import com.rpgproject.domain.exception.NoResultException;
import com.rpgproject.domain.port.presenter.CampaignPresenter;
import com.rpgproject.domain.port.repository.CampaignRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCampaignByNameAndUsernameTest {

	private GetCampaignByNameAndUsername<?> getCampaignByNameAndUsername;

	@Mock
	private CampaignRepository repository;

	@Mock
	private CampaignPresenter<?> presenter;

	@BeforeEach
	public void setUp() {
		getCampaignByNameAndUsername = new GetCampaignByNameAndUsername<>(repository, presenter);
	}

	@Test
	@DisplayName("Given a username and a campaign name When campaign exists Then it gets presented")
	void givenAUsernameAndACampaignName_whenCampaignExists_thenItGetsPresented() {
		// Given
		String username = "alvin.h";
		String name = "Campagne 1";

		Campaign campaign = new Campaign(name, username);

		when(repository.getCampaignByNameAndUsername(name, username)).thenReturn(campaign);

		// When
		getCampaignByNameAndUsername.execute(name, username);

		// Then
		Campaign expectedCampaign = new Campaign("Campagne 1", "alvin.h");

		assertSoftly(softly -> {
			softly.assertThatCode(() -> verify(presenter, times(1)).ok(expectedCampaign)).doesNotThrowAnyException();
		});
	}

	@Test
	@DisplayName("Given a username and a campaign name When campaign does not exist Then not found is presented")
	void givenAUsernameAndACampaignName_whenCampaignDoesNotExist_thenNotFoundIsPresented() {
		// Given
		String username = "alvin.h";
		String name = "Campagne 1";

		when(repository.getCampaignByNameAndUsername(name, username)).thenThrow(new NoResultException());

		// When
		getCampaignByNameAndUsername.execute(name, username);

		// Then
		assertSoftly(softly -> {
			softly.assertThatCode(() -> verify(presenter, times(1)).notFound()).doesNotThrowAnyException();
		});
	}

}