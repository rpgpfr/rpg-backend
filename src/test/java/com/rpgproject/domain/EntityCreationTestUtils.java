package com.rpgproject.domain;

import com.rpgproject.domain.entity.*;
import com.rpgproject.domain.entity.Character;

import java.time.LocalDate;
import java.util.List;

public class EntityCreationTestUtils {

	public static Quest createQuest() {
		return new Quest("alvin", "my-campaign", "title", "main", "description", List.of(new Goal("goal", false)));
	}

	public static List<Quest> createQuests() {
		return List.of(
			new Quest("username", "campagne-1", "quest 1", "main", "description", List.of(new Goal("goal", false))),
			new Quest("username", "campagne-2", "quest 2", "main", "description", List.of(new Goal("goal", false))),
			new Quest("username", "campagne-3", "quest 3", "main", "description", List.of(new Goal("goal", false)))
		);
	}

	public static Campaign createCampaign() {
		return new Campaign("alvin", "My campaign", "my-campaign", null, null, null, null, LocalDate.of(2025, 1, 1));
	}

	public static Campaign createFullCampaign() {
		return new Campaign("alvin", "My campaign", "my-campaign", null, null, null, createQuest(), LocalDate.of(2025, 1, 1));
	}

	public static List<Campaign> createCampaigns() {
		return List.of(
			new Campaign("username", "campagne 1", "campagne-1", null, null, null, null, LocalDate.of(2025, 1, 1)),
			new Campaign("username", "campagne 2", "campagne-2", null, null, null, null, LocalDate.of(2025, 1, 1)),
			new Campaign("username", "campagne 3", "campagne-3", null, null, null, null, LocalDate.of(2025, 1, 1))
		);
	}

	public static UserProfile createUserProfile() {
		return new UserProfile(createUser(), 1, 1, 1, 3);
	}

	public static User createUser() {
		return new User(
			"username",
			"mail@example2.com",
			"firstName",
			"lastName",
			"password",
			null,
			null,
			LocalDate.of(2025, 1, 1)
		);
	}

	public static List<Character> createCharacters() {
		return List.of(
			new Character("username", "campagne-1", "character 1"),
			new Character("username", "campagne-2", "character 2"),
			new Character("username", "campagne-3", "character 3")
		);
	}

	public static Character createCharacter() {
		return new Character("alvin", "my-campaign", "character 1");
	}

}
