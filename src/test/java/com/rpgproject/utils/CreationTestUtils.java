package com.rpgproject.utils;

import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.entity.User;
import com.rpgproject.infrastructure.dto.CampaignDTO;
import com.rpgproject.infrastructure.dto.UserDTO;

import java.util.List;

public class CreationTestUtils {

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
