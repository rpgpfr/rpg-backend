package com.rpgproject.domain;

import com.rpgproject.domain.entity.*;

import java.time.LocalDate;
import java.util.List;

public class EntityCreationTestUtils {

	public static Quest createQuest() {
		return new Quest("title", "main", "description", List.of(new Goal("goal", false)));
	}

	public static List<Quest> createQuests() {
		return List.of(
			new Quest("title", "main", "description", List.of(new Goal("goal", false))),
			new Quest("title", "main", "description", List.of(new Goal("goal", false))),
			new Quest("title", "main", "description", List.of(new Goal("goal", false)))
		);
	}

	public static Campaign createCampaign() {
		return new Campaign("alvin", "My campaign", "my-campaign");
	}

	public static Campaign createFullCampaign() {
		return new Campaign("alvin", "My campaign", "my-campaign");
	}

	public static List<Campaign> createCampaigns() {
		return List.of(
			new Campaign("username", "campagne 1", "campagne-1"),
			new Campaign("username", "campagne 2", "campagne-2"),
			new Campaign("username", "campagne 3", "campagne-3")
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

}
