package com.rpgproject.domain.usecase;

import com.rpgproject.domain.bean.Campaign;
import com.rpgproject.domain.exception.CannotCreateCampaignException;
import com.rpgproject.domain.port.presenter.CampaignPresenter;
import com.rpgproject.domain.port.repository.CampaignRepository;

public class CreateCampaign<T> {

	private final CampaignRepository campaignRepository;
	private final CampaignPresenter<T> presenter;

	public CreateCampaign(CampaignRepository campaignRepository, CampaignPresenter<T> presenter) {
		this.campaignRepository = campaignRepository;
		this.presenter = presenter;
	}

	public T execute(Campaign campaign) {
		try {
			campaignRepository.createCampaign(campaign);

			return presenter.ok();
		} catch (CannotCreateCampaignException e) {
			return presenter.error(e.getMessage());
		}
	}

}
