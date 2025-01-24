package com.rpgproject.domain.usecase;

import com.rpgproject.domain.bean.Campaign;
import com.rpgproject.domain.exception.NoResultException;
import com.rpgproject.domain.port.presenter.CampaignPresenter;
import com.rpgproject.domain.port.repository.CampaignRepository;

public class GetCampaignByNameAndUsername<T> {

	private CampaignRepository repository;
	private CampaignPresenter<T> presenter;

	public GetCampaignByNameAndUsername(CampaignRepository repository, CampaignPresenter<T> presenter) {
		this.repository = repository;
		this.presenter = presenter;
	}

	public T execute(String name, String username) {
		try {
			Campaign campaign = repository.getCampaignByNameAndUsername(name, username);

			return presenter.ok(campaign);
		} catch (NoResultException e) {
			return presenter.notFound();
		}
	}

}
