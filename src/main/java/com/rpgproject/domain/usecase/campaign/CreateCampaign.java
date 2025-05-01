package com.rpgproject.domain.usecase.campaign;

import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.entity.Quest;
import com.rpgproject.domain.exception.DuplicateException;
import com.rpgproject.domain.exception.InternalException;
import com.rpgproject.domain.port.CampaignRepository;
import com.rpgproject.domain.port.Presenter;
import com.rpgproject.domain.port.QuestRepository;

import java.time.LocalDate;

import static com.rpgproject.domain.entity.Quest.createDefaultMainQuest;

public class CreateCampaign<T> {

	private final CampaignRepository campaignRepository;
	private final QuestRepository questRepository;
	private final Presenter<Campaign, T> presenter;

	public CreateCampaign(CampaignRepository campaignRepository, QuestRepository questRepository, Presenter<Campaign, T> presenter) {
		this.campaignRepository = campaignRepository;
		this.questRepository = questRepository;
		this.presenter = presenter;
	}

	public T execute(String owner, String name) {
		try {
			String slug = name.toLowerCase().replace(" ", "-");
			Campaign campaign = new Campaign(owner, name, slug, LocalDate.now());

			campaignRepository.save(campaign);

			Quest quest = createDefaultMainQuest();

			questRepository.save(quest, slug, owner);

			campaign.setMainQuest(quest);

			return presenter.ok();
		} catch (DuplicateException | InternalException e) {
			return presenter.error(e);
		}
	}

}
