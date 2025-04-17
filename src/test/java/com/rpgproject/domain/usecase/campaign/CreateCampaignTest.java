package com.rpgproject.domain.usecase.campaign;

import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.entity.Quest;
import com.rpgproject.domain.exception.campaign.CampaignCreationFailedException;
import com.rpgproject.domain.port.CampaignRepository;
import com.rpgproject.domain.port.Presenter;
import com.rpgproject.domain.port.QuestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCampaignTest {

	private CreateCampaign<?> createCampaign;

	@Mock
	private CampaignRepository campaignRepository;

	@Mock
	private QuestRepository questRepository;

	@Mock
	private Presenter<Campaign, ?> presenter;

	@BeforeEach
	public void setUp() {
		createCampaign = new CreateCampaign<>(campaignRepository, questRepository, presenter);
	}

	@Test
	@DisplayName("Given a campaign, when creating it, then it is saved")
	void givenACampaign_whenCreatingIt_thenItIsSaved() {
		// Given
		String owner = "alvin";
		String name = "myCampaign";

		// When
		createCampaign.execute(owner, name);

		// Then
		Quest expectedQuest = Quest.createDefaultMainQuest();

		verify(campaignRepository, times(1)).save(any(Campaign.class));
		verify(questRepository, times(1)).save(expectedQuest, "mycampaign", owner);
		verify(presenter, times(1)).ok();
	}

	@Test
	@DisplayName("Given a campaign, when creation fails, then an exception is thrown")
	void givenACampaign_whenCreationFails_thenAnExceptionIsThrown() {
		// Given
		String owner = "alvin";
		String name = "myCampaign";
		Campaign campaign = new Campaign(owner, name, "mycampaign", LocalDate.now());
		CampaignCreationFailedException exception = new CampaignCreationFailedException();

		doThrow(exception).when(campaignRepository).save(campaign);

		// When
		createCampaign.execute(owner, name);

		// Then
		Campaign expectedCampaign = new Campaign(owner, name, "mycampaign", LocalDate.now());

		verify(campaignRepository).save(expectedCampaign);
		verify(presenter, times(1)).error(exception);
	}

}