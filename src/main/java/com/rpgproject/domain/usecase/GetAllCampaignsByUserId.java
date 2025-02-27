package com.rpgproject.domain.usecase;

import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.port.CampaignRepository;
import com.rpgproject.domain.port.Presenter;

import java.util.List;

public class GetAllCampaignsByUserId<T> {

	private final CampaignRepository campaignRepository;
	private final Presenter<List<Campaign>, T> campaignsPresenter;

	public GetAllCampaignsByUserId(CampaignRepository campaignRepository, Presenter<List<Campaign>, T> campaignsPresenter) {
		this.campaignRepository = campaignRepository;
		this.campaignsPresenter = campaignsPresenter;
	}

	public T execute(String userId) {
		List<Campaign> campaigns = campaignRepository.getCampaignsByUserId(userId);

		return campaignsPresenter.ok(campaigns);
	}

}
