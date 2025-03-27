package com.rpgproject.domain.usecase.campaign;

import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.exception.CampaignUpdateFailedException;
import com.rpgproject.domain.port.CampaignRepository;
import com.rpgproject.domain.port.Presenter;
import org.apache.commons.lang3.StringUtils;

public class UpdateCampaign<T> {

	private final CampaignRepository campaignRepository;
	private final Presenter<Campaign, T> presenter;

	public UpdateCampaign(CampaignRepository campaignRepository, Presenter<Campaign, T> presenter) {
		this.campaignRepository = campaignRepository;
		this.presenter = presenter;
	}

	public T execute(Campaign campaign, String originalName) {
		try {
			campaignRepository.update(campaign, originalName);
				String newSlug = campaign.name().toLowerCase().replace(" ", "-");
				campaign = new Campaign(newSlug);

			return presenter.ok(campaign);
		} catch (CampaignUpdateFailedException e) {
			return presenter.error(e);
		}
	}

}
