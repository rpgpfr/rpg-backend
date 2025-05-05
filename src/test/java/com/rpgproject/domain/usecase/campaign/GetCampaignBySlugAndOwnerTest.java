package com.rpgproject.domain.usecase.campaign;

import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.entity.Quest;
import com.rpgproject.domain.exception.NotFoundException;
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
	void setUp() {
		getCampaignBySlugAndOwner = new GetCampaignBySlugAndOwner<>(campaignRepository, questRepository, presenter);
	}

	@Test
	@DisplayName("Given a slug and an owner, when the campaign exists, then it is presented")
	void givenASlugAndAnOwner_whenTheCampaignExists_thenItIsPresented() {
		// Given
		Campaign campaign = createCampaign();
		Quest quest = createQuest();
		String slug = campaign.getSlug();
		String owner = campaign.getOwner();

		when(campaignRepository.getCampaignBySlugAndOwner(slug, owner)).thenReturn(campaign);
		when(questRepository.findMainQuestByCampaignSlugAndOwner(slug, owner)).thenReturn(quest);

		// When
		getCampaignBySlugAndOwner.execute(slug, owner);

		// Then
		Campaign expectedCampaign = createFullCampaign();

		verify(presenter, times(1)).ok(expectedCampaign);
	}

	@Test
	@DisplayName("Given a slug and an owner, when the campaign is not found, then an exception is presented")
	void givenASlugAndAnOwner_whenTheCampaignIsNotFound_thenAnExceptionIsPresented() {
		// Given
		Campaign campaign = createCampaign();
		String slug = campaign.getSlug();
		String owner = campaign.getOwner();
		NotFoundException exception = new NotFoundException("not found");

		when(campaignRepository.getCampaignBySlugAndOwner(slug, owner)).thenThrow(exception);

		// When
		getCampaignBySlugAndOwner.execute(slug, owner);

		// Then
		verify(presenter, times(1)).error(exception);
	}

}