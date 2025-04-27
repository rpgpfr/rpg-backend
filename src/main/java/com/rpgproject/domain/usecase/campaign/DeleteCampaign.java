package com.rpgproject.domain.usecase.campaign;

import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.port.CampaignRepository;
import com.rpgproject.domain.port.Presenter;
import com.rpgproject.domain.port.QuestRepository;

public class DeleteCampaign<T> {

	private final CampaignRepository campaignRepository;
	private final QuestRepository questRepository;
	private final Presenter<Campaign, T> presenter;

	public DeleteCampaign(CampaignRepository campaignRepository, QuestRepository questRepository, Presenter<Campaign, T> presenter) {
		this.campaignRepository = campaignRepository;
		this.questRepository = questRepository;
		this.presenter = presenter;
	}

	public T execute(String slug, String owner) {
		try {
			questRepository.deleteBySlugAndOwner(slug, owner);
			campaignRepository.delete(slug, owner);

			return presenter.ok();
		} catch (RuntimeException e) {
			return presenter.error(e);
		}
	}

}
