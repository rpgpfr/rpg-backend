package com.rpgproject.application;

import com.rpgproject.application.dto.viewmodel.CampaignViewModel;
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

	public static List<CampaignViewModel> createCampaignViewModels() {
		return List.of(
			new CampaignViewModel("campagne 1", "campagne-1", null, null, null),
			new CampaignViewModel("campagne 2", "campagne-2", null, null, null),
			new CampaignViewModel("campagne 3", "campagne-3", null, null, null)
		);
	}

	public static CampaignViewModel createCampaignViewModel() {
		return new CampaignViewModel("My campaign", "my-campaign", null, null, null);
	}

}
