package com.rpgproject.application.presenter;

import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.CampaignViewModel;
import com.rpgproject.application.dto.viewmodel.campaign.InfoViewModel;
import com.rpgproject.application.service.ExceptionHTTPStatusService;
import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.port.Presenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CampaignsRestPresenter implements Presenter<List<Campaign>, ResponseEntity<ResponseViewModel<List<CampaignViewModel>>>> {

	private final ExceptionHTTPStatusService exceptionHTTPStatusService;

	@Autowired
	public CampaignsRestPresenter(ExceptionHTTPStatusService exceptionHTTPStatusService) {
		this.exceptionHTTPStatusService = exceptionHTTPStatusService;
	}

	@Override
	public ResponseEntity<ResponseViewModel<List<CampaignViewModel>>> ok() {
		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<ResponseViewModel<List<CampaignViewModel>>> ok(List<Campaign> campaigns) {
		List<CampaignViewModel> campaignViewModels = mapToViewModels(campaigns);

		return ResponseEntity.ok(new ResponseViewModel<>(campaignViewModels, null));
	}

	private List<CampaignViewModel> mapToViewModels(List<Campaign> campaigns) {
		return campaigns
			.stream()
			.map(campaign -> new CampaignViewModel(
				campaign.getName(),
				campaign.getSlug(),
				new InfoViewModel(
					campaign.getDescription(),
					campaign.getType(),
					campaign.getMood()
				),
				null,
				campaign.getCreatedAt()
			))
			.toList();
	}

	@Override
	public ResponseEntity<ResponseViewModel<List<CampaignViewModel>>> error(RuntimeException exception) {
		return new ResponseEntity<>(
			new ResponseViewModel<>(
				null,
				exception.getMessage()
			),
			exceptionHTTPStatusService.getHttpStatusFromExceptionClass(exception)
		);
	}

}
