package com.rpgproject.application.controller;

import com.rpgproject.application.annotation.CurrentOwner;
import com.rpgproject.application.dto.requestbody.CampaignRequestBody;
import com.rpgproject.application.dto.requestbody.CampaignUpdateRequestBody;
import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.CampaignViewModel;
import com.rpgproject.application.presenter.CampaignRestPresenter;
import com.rpgproject.application.presenter.CampaignsRestPresenter;
import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.port.CampaignRepository;
import com.rpgproject.domain.port.QuestRepository;
import com.rpgproject.domain.usecase.campaign.CreateCampaign;
import com.rpgproject.domain.usecase.campaign.GetAllCampaignsByOwner;
import com.rpgproject.domain.usecase.campaign.GetCampaignBySlugAndOwner;
import com.rpgproject.domain.usecase.campaign.UpdateCampaign;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/campaign")
@Controller
public class CampaignController {

	private final GetAllCampaignsByOwner<ResponseEntity<ResponseViewModel<List<CampaignViewModel>>>> getAllCampaignsByOwner;
	private final CreateCampaign<ResponseEntity<ResponseViewModel<CampaignViewModel>>> createCampaign;
	private final GetCampaignBySlugAndOwner<ResponseEntity<ResponseViewModel<CampaignViewModel>>> getCampaignBySlugAndOwner;
	private final UpdateCampaign<ResponseEntity<ResponseViewModel<CampaignViewModel>>> updateCampaign;

	public CampaignController(QuestRepository questRepository, CampaignRepository campaignRepository, CampaignsRestPresenter campaignsRestPresenter, CampaignRestPresenter campaignRestPresenter) {
		this.getAllCampaignsByOwner = new GetAllCampaignsByOwner<>(campaignRepository, campaignsRestPresenter);
		this.createCampaign = new CreateCampaign<>(campaignRepository, questRepository, campaignRestPresenter);
		this.getCampaignBySlugAndOwner = new GetCampaignBySlugAndOwner<>(campaignRepository, questRepository, campaignRestPresenter);
		this.updateCampaign = new UpdateCampaign<>(campaignRepository, campaignRestPresenter);
	}

	@GetMapping("/")
	@CrossOrigin(origins = "*")
	public ResponseEntity<ResponseViewModel<List<CampaignViewModel>>> getAllCampaignsByOwner(@CurrentOwner String owner) {
		return getAllCampaignsByOwner.execute(owner);
	}

	@PostMapping("/")
	@CrossOrigin(origins = "*")
	public ResponseEntity<ResponseViewModel<CampaignViewModel>> createCampaign(@CurrentOwner String owner, @RequestBody CampaignRequestBody campaignRequestBody) {
		return createCampaign.execute(owner, campaignRequestBody.name());
	}

	@GetMapping("/{slug}")
	@CrossOrigin(origins = "*")
	public ResponseEntity<ResponseViewModel<CampaignViewModel>> getCampaign(@CurrentOwner String owner, @PathVariable String slug) {
		return getCampaignBySlugAndOwner.execute(slug, owner);
	}

	@PatchMapping("/{slug}")
	@CrossOrigin(origins = "*")
	public ResponseEntity<ResponseViewModel<CampaignViewModel>> updateCampaign(@CurrentOwner String owner, @PathVariable String slug, @RequestBody CampaignUpdateRequestBody campaignUpdateRequestBody) {
		Campaign campaign = new Campaign(
			owner,
			campaignUpdateRequestBody.name(),
			slug,
			campaignUpdateRequestBody.description(),
			campaignUpdateRequestBody.type(),
			campaignUpdateRequestBody.mood(),
			null
		);

		return updateCampaign.execute(campaign, slug);
	}

}
