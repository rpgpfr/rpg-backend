package com.rpgproject.application.presenter;

import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.CampaignViewModel;
import com.rpgproject.application.dto.viewmodel.GoalViewModel;
import com.rpgproject.application.dto.viewmodel.QuestViewModel;
import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.port.Presenter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CampaignRestPresenter implements Presenter<Campaign, ResponseEntity<ResponseViewModel<CampaignViewModel>>> {

	@Override
	public ResponseEntity<ResponseViewModel<CampaignViewModel>> ok() {
		return ResponseEntity.ok().build();
	}

	@Override
	public ResponseEntity<ResponseViewModel<CampaignViewModel>> ok(Campaign campaign) {
		QuestViewModel mainQuestViewModel = null;

		if (campaign.getMainQuest() != null) {
			mainQuestViewModel = mapToQuestViewModel(campaign);
		}

		return ResponseEntity.ok(
			new ResponseViewModel<>(
				new CampaignViewModel(
					campaign.getName(),
					campaign.getSlug(),
					campaign.getDescription(),
					campaign.getType(),
					campaign.getMood(),
					mainQuestViewModel
				),
				null
			)
		);
	}

	private QuestViewModel mapToQuestViewModel(Campaign campaign) {
		return new QuestViewModel(
			campaign.getMainQuest().title(),
			campaign.getMainQuest().type(),
			campaign.getMainQuest().description(),
			mapToGoalViewModels(campaign)
		);
	}

	private List<GoalViewModel> mapToGoalViewModels(Campaign campaign) {
		return campaign.getMainQuest().goals().stream()
			.map(goal -> new GoalViewModel(goal.name(), goal.completed()))
			.toList();
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
