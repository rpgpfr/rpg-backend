package com.rpgproject.domain.usecase.campaign;

import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.entity.Quest;
import com.rpgproject.domain.exception.NotFoundException;
import com.rpgproject.domain.port.CampaignRepository;
import com.rpgproject.domain.port.Presenter;
import com.rpgproject.domain.port.QuestRepository;

public class GetCampaignBySlugAndOwner<T> {

	private final CampaignRepository campaignRepository;
	private final QuestRepository questRepository;
	private final Presenter<Campaign, T> presenter;

	public GetCampaignBySlugAndOwner(CampaignRepository campaignRepository, QuestRepository questRepository, Presenter<Campaign, T> presenter) {
		this.campaignRepository = campaignRepository;
		this.questRepository = questRepository;
		this.presenter = presenter;
	}

	public T execute(String slug, String owner) {
		try {
			Campaign campaign = campaignRepository.getCampaignBySlugAndOwner(slug, owner);
			Quest campaignQuest = questRepository.findMainQuestBySlugAndOwner(slug, owner);

			campaign.setMainQuest(campaignQuest);

			return presenter.ok(campaign);
		} catch (NotFoundException e) {
			return presenter.error(e);
		}
	}

}
