package com.rpgproject.domain.port.presenter;

import com.rpgproject.domain.bean.Campaign;

public interface CampaignPresenter<T> {

	T ok();
	T ok(Campaign campaign);
	T error(String message);
	T notFound();

}
