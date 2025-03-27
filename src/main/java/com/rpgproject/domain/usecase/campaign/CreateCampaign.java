package com.rpgproject.domain.usecase.campaign;

import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.exception.CampaignCreationFailedException;
import com.rpgproject.domain.port.CampaignRepository;
import com.rpgproject.domain.port.Presenter;

public class CreateCampaign<T> {

	private final CampaignRepository campaignRepository;
	private final Presenter<Campaign, T> presenter;

	public CreateCampaign(CampaignRepository campaignRepository, Presenter<Campaign, T> presenter) {
		this.campaignRepository = campaignRepository;
		this.presenter = presenter;
	}

	public T execute(String owner, String name) {
		try {
			String slug = name.toLowerCase().replace(" ", "-");
			Campaign campaign = new Campaign(owner, name, slug);
			campaignRepository.save(campaign);

			return presenter.ok();
		} catch (CampaignCreationFailedException e) {
			return presenter.error(e);
		}
	}

}
