package com.rpgproject.application.controller;

import com.rpgproject.application.annotation.CurrentOwner;
import com.rpgproject.application.dto.requestbody.CampaignRequestBody;
import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.CampaignViewModel;
import com.rpgproject.application.presenter.CampaignRestPresenter;
import com.rpgproject.application.presenter.CampaignsRestPresenter;
import com.rpgproject.domain.port.CampaignRepository;
import com.rpgproject.domain.usecase.campaign.CreateCampaign;
import com.rpgproject.domain.usecase.campaign.GetAllCampaignsByOwner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/campaign")
@Controller
public class CampaignController {

	private final GetAllCampaignsByOwner<ResponseEntity<ResponseViewModel<List<CampaignViewModel>>>> getAllCampaignsByOwner;
	private final CreateCampaign<ResponseEntity<ResponseViewModel<CampaignViewModel>>> createCampaign;

	public CampaignController(CampaignRepository campaignRepository, CampaignsRestPresenter campaignsRestPresenter, CampaignRestPresenter campaignRestPresenter) {
		this.getAllCampaignsByOwner = new GetAllCampaignsByOwner<>(campaignRepository, campaignsRestPresenter);
		this.createCampaign = new CreateCampaign<>(campaignRepository, campaignRestPresenter);
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

}
