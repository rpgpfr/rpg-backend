package com.rpgproject.domain.usecase.campaign;

import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.exception.InternalException;
import com.rpgproject.domain.port.CampaignRepository;
import com.rpgproject.domain.port.CharacterRepository;
import com.rpgproject.domain.port.Presenter;
import com.rpgproject.domain.port.QuestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.rpgproject.domain.EntityCreationTestUtils.createCampaign;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteCampaignTest {

	private DeleteCampaign<?> deleteCampaign;

	@Mock
	private CampaignRepository campaignRepository;

	@Mock
	QuestRepository questRepository;

	@Mock
	CharacterRepository characterRepository;

	@Mock
	private Presenter<Campaign, ?> presenter;

	@BeforeEach
	void setUp() {
		deleteCampaign = new DeleteCampaign<>(campaignRepository, questRepository, characterRepository, presenter);
	}

	@Test
	@DisplayName("Given a campaign, when it exists, then a success is presented")
	void givenACampaign_whenItExists_thenASuccessIsPresented() {
		// Given
		Campaign campaign = createCampaign();
		String slug = campaign.getSlug();
		String owner = campaign.getOwner();

		// When
		deleteCampaign.execute(slug, owner);

		// Then
		verify(questRepository, times(1)).deleteAllByCampaignSlugAndOwner(slug, owner);
		verify(characterRepository, times(1)).deleteAllByCampaignSlugAndOwner(slug, owner);
		verify(campaignRepository, times(1)).delete(slug, owner);
		verify(presenter, times(1)).ok();
	}

	@Test
	@DisplayName("Given a campaign, when delete fails, then an exception is presented")
	void givenACampaign_whenDeleteFails_thenAnExceptionIsThrown() {
		// Given
		Campaign campaign = createCampaign();
		String slug = campaign.getSlug();
		String owner = campaign.getOwner();
		InternalException exception = new InternalException("error");

		doThrow(exception).when(campaignRepository).delete(slug, owner);

		// When
		deleteCampaign.execute(slug, owner);

		// Then
		verify(questRepository, times(1)).deleteAllByCampaignSlugAndOwner(slug, owner);
		verify(characterRepository, times(1)).deleteAllByCampaignSlugAndOwner(slug, owner);
		verify(campaignRepository, times(1)).delete(slug, owner);
		verify(presenter, times(1)).error(exception);
	}

}