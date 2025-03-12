package com.rpgproject.utils;

import com.rpgproject.application.dto.viewmodel.CampaignViewModel;
import com.rpgproject.application.dto.viewmodel.UserViewModel;
import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.entity.User;
import com.rpgproject.domain.entity.UserProfile;
import com.rpgproject.infrastructure.dto.CampaignDTO;
import com.rpgproject.infrastructure.dto.CharacterDTO;
import com.rpgproject.infrastructure.dto.MapDTO;
import com.rpgproject.infrastructure.dto.UserDTO;

import java.util.List;

public class CreationTestUtils {

	public static UserProfile createUserProfile() {
		return new UserProfile(createUser(), 1, 1, 1, 3);
	}

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

	public static User createUser() {
		return new User(
			"username",
			"mail@example2.com",
			"firstName",
			"lastName",
			"password"
		);
	}

	public static User createUserWithEmail() {
		return new User(
			null,
			"mail@example2.com",
			"firstName",
			"lastName",
			"password"
		);
	}

	public static User createUserWithUsername() {
		return new User(
			"username",
			null,
			"firstName",
			"lastName",
			"password"
		);
	}

	public static UserDTO createUserDTO(String firstName, String lastName, String password, String introduction, String rpgKnowledge) {
		return new UserDTO(
			"username",
			"mail@example2.com",
			firstName,
			lastName,
			password,
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
			new Campaign("username", "campagne 1"),
			new Campaign("username", "campagne 2"),
			new Campaign("username", "campagne 3")
		);
	}

	public static List<CampaignDTO> createCampaignDTOs() {
		return List.of(
			new CampaignDTO("username", "campagne 1"),
			new CampaignDTO("username", "campagne 2"),
			new CampaignDTO("username", "campagne 3")
		);
	}

	public static List<MapDTO> createMapDTOs() {
		return List.of(
			new MapDTO("username", "map 1"),
			new MapDTO("username", "map 2"),
			new MapDTO("username", "map 3")
		);
	}

	public static List<CharacterDTO> createCharacterDTOs() {
		return List.of(
			new CharacterDTO("username", "character 1"),
			new CharacterDTO("username", "character 2"),
			new CharacterDTO("username", "character 3")
		);
	}

}
