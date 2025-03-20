package com.rpgproject.domain.usecase.campaign;

import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.port.CampaignRepository;
import com.rpgproject.domain.port.Presenter;

import java.util.List;

public class GetAllCampaignsByOwner<T> {

	private final CampaignRepository campaignRepository;
	private final Presenter<List<Campaign>, T> campaignsPresenter;

	public GetAllCampaignsByOwner(CampaignRepository campaignRepository, Presenter<List<Campaign>, T> campaignsPresenter) {
		this.campaignRepository = campaignRepository;
		this.campaignsPresenter = campaignsPresenter;
	}

	public T execute(String owner) {
		List<Campaign> campaigns = campaignRepository.getCampaignsByOwner(owner);

		return campaignsPresenter.ok(campaigns);
	}

}
