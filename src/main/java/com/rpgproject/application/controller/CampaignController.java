package com.rpgproject.application.controller;

import com.rpgproject.application.annotation.CurrentOwner;
import com.rpgproject.application.dto.requestbody.CampaignRequestBody;
import com.rpgproject.application.dto.requestbody.CampaignUpdateRequestBody;
import com.rpgproject.application.dto.requestbody.QuestUpdateRequestBody;
import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.CampaignViewModel;
import com.rpgproject.application.dto.viewmodel.QuestViewModel;
import com.rpgproject.application.presenter.CampaignRestPresenter;
import com.rpgproject.application.presenter.CampaignsRestPresenter;
import com.rpgproject.application.presenter.QuestRestPresenter;
import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.entity.Goal;
import com.rpgproject.domain.entity.Quest;
import com.rpgproject.domain.port.CampaignRepository;
import com.rpgproject.domain.port.QuestRepository;
import com.rpgproject.domain.usecase.campaign.*;
import com.rpgproject.domain.usecase.quest.UpdateMainQuest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/campaigns")
public class CampaignController {

	private final GetCampaignsByOwner<ResponseEntity<ResponseViewModel<List<CampaignViewModel>>>> getCampaignsByOwner;
	private final CreateCampaign<ResponseEntity<ResponseViewModel<CampaignViewModel>>> createCampaign;
	private final GetCampaignBySlugAndOwner<ResponseEntity<ResponseViewModel<CampaignViewModel>>> getCampaignBySlugAndOwner;
	private final UpdateCampaign<ResponseEntity<ResponseViewModel<CampaignViewModel>>> updateCampaign;
	private final DeleteCampaign<ResponseEntity<ResponseViewModel<CampaignViewModel>>> deleteCampaign;
	private final UpdateMainQuest<ResponseEntity<ResponseViewModel<QuestViewModel>>> updateMainQuest;

	public CampaignController(QuestRepository questRepository, CampaignRepository campaignRepository, CampaignsRestPresenter campaignsRestPresenter, CampaignRestPresenter campaignRestPresenter, QuestRestPresenter questRestPresenter) {
		this.getCampaignsByOwner = new GetCampaignsByOwner<>(campaignRepository, campaignsRestPresenter);
		this.createCampaign = new CreateCampaign<>(campaignRepository, questRepository, campaignRestPresenter);
		this.getCampaignBySlugAndOwner = new GetCampaignBySlugAndOwner<>(campaignRepository, questRepository, campaignRestPresenter);
		this.updateCampaign = new UpdateCampaign<>(campaignRepository, campaignRestPresenter);
		this.deleteCampaign = new DeleteCampaign<>(campaignRepository, questRepository, campaignRestPresenter);
		this.updateMainQuest = new UpdateMainQuest<>(questRepository, questRestPresenter);
	}

	@GetMapping("")
	@CrossOrigin(origins = "*")
	public ResponseEntity<ResponseViewModel<List<CampaignViewModel>>> getAllCampaignsByOwner(@CurrentOwner String owner) {
		return getCampaignsByOwner.execute(owner);
	}

	@PostMapping("")
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
			null,
			slug,
			campaignUpdateRequestBody.description(),
			campaignUpdateRequestBody.type(),
			campaignUpdateRequestBody.mood(),
			null,
			null
		);

		return updateCampaign.execute(campaign, slug);
	}

	@DeleteMapping("/{slug}")
	@CrossOrigin(origins = "*")
	public ResponseEntity<ResponseViewModel<CampaignViewModel>> deleteCampaign(@CurrentOwner String owner, @PathVariable String slug) {
		return deleteCampaign.execute(slug, owner);
	}

	@PatchMapping("/{slug}/mainQuest")
	@CrossOrigin(origins = "*")
	public ResponseEntity<ResponseViewModel<QuestViewModel>> updateQuest(@CurrentOwner String owner, @PathVariable String slug, @RequestBody QuestUpdateRequestBody questUpdateRequestBody) {
		Quest quest = new Quest(
			questUpdateRequestBody.title(),
			questUpdateRequestBody.type(),
			questUpdateRequestBody.description(),
			questUpdateRequestBody.goals()
				.stream()
				.map(goal -> new Goal(
					goal.name(),
					goal.completed()
				))
				.toList()
		);

		return updateMainQuest.execute(quest, slug, owner);
	}

}
