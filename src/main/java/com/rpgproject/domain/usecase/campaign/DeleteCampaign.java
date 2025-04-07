package com.rpgproject.domain.usecase.campaign;

import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.port.CampaignRepository;
import com.rpgproject.domain.port.Presenter;

public class DeleteCampaign<T> {

	private final CampaignRepository campaignRepository;
	private final Presenter<Campaign, T> presenter;

	public DeleteCampaign(CampaignRepository campaignRepository, Presenter<Campaign, T> presenter) {
		this.campaignRepository = campaignRepository;
		this.presenter = presenter;
	}

	public T execute(String slug, String owner) {
		try {
			campaignRepository.delete(slug, owner);

			return presenter.ok();
		} catch (Exception e) {
			return presenter.error(e);
		}
	}

}
