package com.rpgproject.application;

import com.rpgproject.application.dto.viewmodel.CampaignViewModel;
import com.rpgproject.application.dto.viewmodel.GoalViewModel;
import com.rpgproject.application.dto.viewmodel.QuestViewModel;
import com.rpgproject.application.dto.viewmodel.UserViewModel;

import java.time.LocalDate;
import java.util.List;

public class DTOCreationTestUtils {

	public static UserViewModel createUserViewModel() {
		return new UserViewModel(
			"username",
			"mail@example2.com",
			"firstName",
			"lastName",
			null,
			null
		);
	}

	public static List<QuestViewModel> createQuestViewModels() {
		return List.of(
			new QuestViewModel("quest 1", "main", "description", List.of(new GoalViewModel("goal", false))),
			new QuestViewModel("quest 2", "main", "description", List.of(new GoalViewModel("goal", false))),
			new QuestViewModel("quest 3", "main", "description", List.of(new GoalViewModel("goal", false)))
		);
	}

	public static QuestViewModel createQuestViewModel() {
		return new QuestViewModel("title", "main", "description", List.of(new GoalViewModel("goal", false)));
	}

	public static List<CampaignViewModel> createCampaignViewModels() {
		return List.of(
			new CampaignViewModel("campagne 1", "campagne-1", null, null, null, null, LocalDate.of(2025, 1, 1)),
			new CampaignViewModel("campagne 2", "campagne-2", null, null, null, null, LocalDate.of(2025, 1, 1)),
			new CampaignViewModel("campagne 3", "campagne-3", null, null, null, null, LocalDate.of(2025, 1, 1))
		);
	}

	public static List<CampaignViewModel> createFullCampaignViewModels() {
		List<QuestViewModel> questViewModels = createQuestViewModels();
		return List.of(
			new CampaignViewModel("campagne 1", "campagne-1", null, null, null, questViewModels.getFirst(), LocalDate.of(2025, 1, 1)),
			new CampaignViewModel("campagne 2", "campagne-2", null, null, null, questViewModels.get(1), LocalDate.of(2025, 1, 1)),
			new CampaignViewModel("campagne 3", "campagne-3", null, null, null, questViewModels.getLast(), LocalDate.of(2025, 1, 1))
		);
	}

	public static CampaignViewModel createCampaignViewModel() {
		return new CampaignViewModel("My campaign", "my-campaign", null, null, null, null, LocalDate.of(2025, 1, 1));
	}

	public static CampaignViewModel createFullCampaignViewModel() {
		return new CampaignViewModel("My campaign", "my-campaign", null, null, null, createQuestViewModel(), LocalDate.of(2025, 1, 1));
	}

}
