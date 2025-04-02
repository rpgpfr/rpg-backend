package com.rpgproject.application;

import com.rpgproject.application.dto.viewmodel.CampaignViewModel;
import com.rpgproject.application.dto.viewmodel.GoalViewModel;
import com.rpgproject.application.dto.viewmodel.QuestViewModel;
import com.rpgproject.application.dto.viewmodel.UserViewModel;

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

	public static List<CampaignViewModel> createCampaignViewModels() {
		return List.of(
			new CampaignViewModel("campagne 1", "campagne-1", null, null, null, null),
			new CampaignViewModel("campagne 2", "campagne-2", null, null, null, null),
			new CampaignViewModel("campagne 3", "campagne-3", null, null, null, null)
		);
	}

	public static List<CampaignViewModel> createFullCampaignViewModels() {
		List<QuestViewModel> questViewModels = createQuestViewModels();
		return List.of(
			new CampaignViewModel("campagne 1", "campagne-1", null, null, null, questViewModels.getFirst()),
			new CampaignViewModel("campagne 2", "campagne-2", null, null, null, questViewModels.get(1)),
			new CampaignViewModel("campagne 3", "campagne-3", null, null, null, questViewModels.getLast())
		);
	}

	public static CampaignViewModel createCampaignViewModel() {
		return new CampaignViewModel("My campaign", "my-campaign", null, null, null, null);
	}

}
