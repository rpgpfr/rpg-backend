package com.rpgproject.domain.usecase.campaign;

import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.exception.CampaignUpdateFailedException;
import com.rpgproject.domain.port.CampaignRepository;
import com.rpgproject.domain.port.Presenter;

public class UpdateCampaign<T> {

	private final CampaignRepository campaignRepository;
	private final Presenter<Campaign, T> presenter;

	public UpdateCampaign(CampaignRepository campaignRepository, Presenter<Campaign, T> presenter) {
		this.campaignRepository = campaignRepository;
		this.presenter = presenter;
	}

	public void execute(Campaign campaign, String originalName) {
		try {
			campaignRepository.update(campaign, originalName);
			presenter.ok();
		} catch (CampaignUpdateFailedException e) {
			presenter.error(e);
		}

	}

}
