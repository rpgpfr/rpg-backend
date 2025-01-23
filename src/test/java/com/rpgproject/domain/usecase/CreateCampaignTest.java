package com.rpgproject.domain.usecase;

import com.rpgproject.domain.bean.Campaign;
import com.rpgproject.domain.exception.CannotCreateCampaignException;
import com.rpgproject.domain.port.presenter.CampaignPresenter;
import com.rpgproject.domain.port.repository.CampaignRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCampaignTest {

	private CreateCampaign<?> createCampaign;

	@Mock
	private CampaignRepository repository;

	@Mock
	private CampaignPresenter<?> presenter;

	@BeforeEach
	public void setUp() {
		createCampaign = new CreateCampaign<>(repository, presenter);
	}

	@Test
	@DisplayName("Given a campaign When it does not exist Then it gets created")
	void givenACampaign_whenItDoesNotExist_thenItGetsCreated() {
		// Given
		Campaign campaign = new Campaign("Campagne", "alvin.h");

		// When
		createCampaign.execute(campaign);

		// Then
		assertSoftly(softly -> {
			softly.assertThatCode(() -> verify(repository, times(1)).createCampaign(campaign)).doesNotThrowAnyException();
			softly.assertThatCode(() -> verify(presenter, times(1)).ok()).doesNotThrowAnyException();
		});
	}

	@Test
	@SneakyThrows
	@DisplayName("Given a campaign When creation fails Then an error is returned")
	void givenACampaign_whenCreationFails_thenAnErrorIsReturned() {
		// Given
		Campaign campaign = new Campaign("Campagne", "alvin.h");

		doThrow(new CannotCreateCampaignException("already exists")).when(repository).createCampaign(campaign);

		// When
		createCampaign.execute(campaign);

		// Then
		assertSoftly(softly -> {
			softly.assertThatCode(() -> repository.createCampaign(campaign)).isInstanceOf(CannotCreateCampaignException.class);
			softly.assertThatCode(() -> verify(presenter, times(1)).error("already exists")).doesNotThrowAnyException();
		});
	}

}