package com.rpgproject.domain.port;

import com.rpgproject.domain.entity.Campaign;

public interface CampaignPresenter<T> {

	void present(Campaign campaign);

}
