package com.rpgproject.domain.usecase.campaign;

import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.entity.Quest;
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

import static com.rpgproject.domain.EntityCreationTestUtils.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCampaignBySlugAndOwnerTest {

	private GetCampaignBySlugAndOwner<?> getCampaignBySlugAndOwner;

	@Mock
	private CampaignRepository campaignRepository;

	@Mock
	private QuestRepository questRepository;

	@Mock
	private Presenter<Campaign, ?> presenter;

	@BeforeEach
	public void setUp() {
		getCampaignBySlugAndOwner = new GetCampaignBySlugAndOwner<>(campaignRepository, questRepository, presenter);
	}

	@Test
	@DisplayName("Given a slug and an owner, when getting a campaign, then it is presented")
	void givenASlugAndAnOwner_whenGettingACampaign_thenItIsPresented() {
		// Given
		Campaign campaign = createCampaign();
		Quest quest = createQuest();
		String slug = campaign.getSlug();
		String owner = campaign.getOwner();

		when(campaignRepository.getCampaignBySlugAndOwner(slug, owner)).thenReturn(campaign);
		when(questRepository.findMainQuestBySlugAndOwner(slug, owner)).thenReturn(quest);

		// When
		getCampaignBySlugAndOwner.execute(slug, owner);

		// Then
		Campaign expectedCampaign = createFullCampaign();

		verify(presenter, times(1)).ok(expectedCampaign);
	}

	@Test
	@DisplayName("Given a slug and an owner, when getting a campaign, then an exception is presented")
	void givenASlugAndAnOwner_whenGettingACampaign_thenAnExceptionIsPresented() {
		// Given
		Campaign campaign = createCampaign();
		String slug = campaign.getSlug();
		String owner = campaign.getOwner();
		CampaignNotFoundException campaignNotFoundException = new CampaignNotFoundException();

		when(campaignRepository.getCampaignBySlugAndOwner(slug, owner)).thenThrow(campaignNotFoundException);

		// When
		getCampaignBySlugAndOwner.execute(slug, owner);

		// Then
		verify(presenter, times(1)).error(any(CampaignNotFoundException.class));
	}

}