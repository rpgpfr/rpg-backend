package com.rpgproject.application.presenter;

import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.GoalViewModel;
import com.rpgproject.application.dto.viewmodel.QuestViewModel;
import com.rpgproject.application.service.ExceptionHTTPStatusService;
import com.rpgproject.domain.entity.Goal;
import com.rpgproject.domain.entity.Quest;
import com.rpgproject.domain.port.Presenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuestRestPresenter implements Presenter<Quest, ResponseEntity<ResponseViewModel<QuestViewModel>>> {

	private final ExceptionHTTPStatusService exceptionHTTPStatusService;

	@Autowired
	public QuestRestPresenter(ExceptionHTTPStatusService exceptionHTTPStatusService) {
		this.exceptionHTTPStatusService = exceptionHTTPStatusService;
	}

	@Override
	public ResponseEntity<ResponseViewModel<QuestViewModel>> ok() {
		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<ResponseViewModel<QuestViewModel>> ok(Quest quest) {
		return ResponseEntity.ok(
			new ResponseViewModel<>(
				new QuestViewModel(
					quest.title(),
					quest.type(),
					quest.description(),
					mapToGoalViewModels(quest.goals())
				),
				null
			)
		);
	}

	private List<GoalViewModel> mapToGoalViewModels(List<Goal> goals) {
		return goals.stream()
			.map(goal -> new GoalViewModel(goal.name(), goal.completed()))
			.toList();
	}

	@Override
	public ResponseEntity<ResponseViewModel<QuestViewModel>> error(RuntimeException exception) {
		return new ResponseEntity<>(
			new ResponseViewModel<>(
				null,
				exception.getMessage()
			),
			exceptionHTTPStatusService.getHttpStatusFromExceptionClass(exception)
		);
	}
}
