package com.rpgproject.application.presenter;

import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.CampaignViewModel;
import com.rpgproject.application.dto.viewmodel.GoalViewModel;
import com.rpgproject.application.dto.viewmodel.QuestViewModel;
import com.rpgproject.application.dto.viewmodel.campaign.InfoViewModel;
import com.rpgproject.application.service.ExceptionHTTPStatusService;
import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.entity.Goal;
import com.rpgproject.domain.entity.Quest;
import com.rpgproject.domain.port.Presenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CampaignRestPresenter implements Presenter<Campaign, ResponseEntity<ResponseViewModel<CampaignViewModel>>> {

	private final ExceptionHTTPStatusService exceptionHTTPStatusService;

	@Autowired
	public CampaignRestPresenter(ExceptionHTTPStatusService exceptionHTTPStatusService) {
		this.exceptionHTTPStatusService = exceptionHTTPStatusService;
	}

	@Override
	public ResponseEntity<ResponseViewModel<CampaignViewModel>> ok() {
		return ResponseEntity.ok().build();
	}

	@Override
	public ResponseEntity<ResponseViewModel<CampaignViewModel>> ok(Campaign campaign) {
		QuestViewModel mainQuestViewModel = null;

		if (campaign.getMainQuest() != null) {
			mainQuestViewModel = mapToQuestViewModel(campaign.getMainQuest());
		}

		return ResponseEntity.ok(
			new ResponseViewModel<>(
				new CampaignViewModel(
					campaign.getName(),
					campaign.getSlug(),
					new InfoViewModel(
						campaign.getDescription(),
						campaign.getType(),
						campaign.getMood()
					),
					mainQuestViewModel,
					campaign.getCreatedAt()
				),
				null
			)
		);
	}

	private QuestViewModel mapToQuestViewModel(Quest quest) {
		return new QuestViewModel(
			quest.title(),
			quest.type(),
			quest.description(),
			mapToGoalViewModels(quest.goals())
		);
	}

	private List<GoalViewModel> mapToGoalViewModels(List<Goal> goals) {
		return goals.stream()
			.map(goal -> new GoalViewModel(goal.name(), goal.completed()))
			.toList();
	}

	@Override
	public ResponseEntity<ResponseViewModel<CampaignViewModel>> error(RuntimeException exception) {
		return new ResponseEntity<>(
			new ResponseViewModel<>(
				null,
				exception.getMessage()
			),
			exceptionHTTPStatusService.getHttpStatusFromExceptionClass(exception)
		);
	}

}
