package com.rpgproject.application.presenter;

import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.CampaignViewModel;
import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.port.Presenter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CampaignRestPresenter implements Presenter<Campaign, ResponseEntity<ResponseViewModel<CampaignViewModel>>> {

	@Override
	public ResponseEntity<ResponseViewModel<CampaignViewModel>> ok() {
		return ResponseEntity.ok().build();
	}

	@Override
	public ResponseEntity<ResponseViewModel<CampaignViewModel>> ok(Campaign campaign) {
		return ResponseEntity.ok(
			new ResponseViewModel<>(
				new CampaignViewModel(
					campaign.getName(),
					campaign.getSlug(),
					campaign.getDescription(),
					campaign.getType(),
					campaign.getMood()
				),
				null
			)
		);
	}

	@Override
	public ResponseEntity<ResponseViewModel<CampaignViewModel>> error(Exception exception) {
		return ResponseEntity
			.badRequest()
			.body(
				new ResponseViewModel<>(null, exception.getMessage())
			);
	}

}
