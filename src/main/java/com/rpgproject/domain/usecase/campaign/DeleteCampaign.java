package com.rpgproject.domain.usecase.campaign;

import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.exception.InternalException;
import com.rpgproject.domain.exception.NotFoundException;
import com.rpgproject.domain.port.CampaignRepository;
import com.rpgproject.domain.port.CharacterRepository;
import com.rpgproject.domain.port.Presenter;
import com.rpgproject.domain.port.QuestRepository;

public class DeleteCampaign<T> {

	private final CampaignRepository campaignRepository;
	private final QuestRepository questRepository;
	private final CharacterRepository characterRepository;
	private final Presenter<Campaign, T> presenter;

	public DeleteCampaign(CampaignRepository campaignRepository, QuestRepository questRepository, CharacterRepository characterRepository, Presenter<Campaign, T> presenter) {
		this.campaignRepository = campaignRepository;
		this.questRepository = questRepository;
		this.characterRepository = characterRepository;
		this.presenter = presenter;
	}

	public T execute(String slug, String owner) {
		try {
			questRepository.deleteAllByCampaignSlugAndOwner(slug, owner);
			characterRepository.deleteAllByCampaignSlugAndOwner(slug, owner);
			campaignRepository.delete(slug, owner);

			return presenter.ok();
		} catch (NotFoundException | InternalException e) {
			return presenter.error(e);
		}
	}

}
