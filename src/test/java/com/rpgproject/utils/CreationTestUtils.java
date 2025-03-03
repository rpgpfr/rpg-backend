package com.rpgproject.utils;

import com.rpgproject.application.dto.viewmodel.CampaignViewModel;
import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.entity.User;
import com.rpgproject.domain.entity.UserProfile;
import com.rpgproject.infrastructure.dto.CampaignDTO;
import com.rpgproject.infrastructure.dto.UserDTO;

import java.util.List;

public class CreationTestUtils {

	public static UserProfile createUserProfile() {
		return new UserProfile(createUser(), 1, 1, 1, 3);
	}

	public static User createUser() {
		return new User(
			"uniqueName",
			"username"
		);
	}

	public static UserDTO createUserDTO(String firstName, String lastName, String introduction, String rpgKnowledge) {
		return new UserDTO(
			"uniqueName",
			"username",
			firstName,
			lastName,
			introduction,
			rpgKnowledge
		);
	}

	public static List<CampaignViewModel> createCampaignViewModels() {
		return List.of(
			new CampaignViewModel("campagne 1"),
			new CampaignViewModel("campagne 2"),
			new CampaignViewModel("campagne 3")
		);
	}

	public static List<Campaign> createCampaigns() {
		return List.of(
			new Campaign("campagne 1"),
			new Campaign("campagne 2"),
			new Campaign("campagne 3")
		);
	}

	public static List<CampaignDTO> createCampaignDTOs() {
		return List.of(
			new CampaignDTO("username", "campagne 1"),
			new CampaignDTO("username", "campagne 2"),
			new CampaignDTO("username", "campagne 3")
		);
	}

}
