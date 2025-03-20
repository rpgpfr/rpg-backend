package com.rpgproject.application.controller;

import com.rpgproject.application.annotation.CurrentOwner;
import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.CampaignViewModel;
import com.rpgproject.application.presenter.CampaignsRestPresenter;
import com.rpgproject.domain.port.CampaignRepository;
import com.rpgproject.domain.usecase.campaign.GetAllCampaignsByOwner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/campaign")
@Controller
public class CampaignController {

	private final GetAllCampaignsByOwner<ResponseEntity<ResponseViewModel<List<CampaignViewModel>>>> getAllCampaignsByOwner;

	public CampaignController(CampaignRepository campaignRepository, CampaignsRestPresenter campaignsRestPresenter) {
		this.getAllCampaignsByOwner = new GetAllCampaignsByOwner<>(campaignRepository, campaignsRestPresenter);
	}

	@GetMapping("/")
	@CrossOrigin(origins = "*")
	public ResponseEntity<ResponseViewModel<List<CampaignViewModel>>> getAllCampaignsByOwner(@CurrentOwner String owner) {
		return getAllCampaignsByOwner.execute(owner);
	}

}
