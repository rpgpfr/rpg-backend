package com.rpgproject.infrastructure;

import com.rpgproject.infrastructure.dto.*;

import java.time.LocalDate;
import java.util.List;

public class DTOCreationTestUtils {

	public static UserDTO createUserDTO(String firstName, String lastName, String password, String introduction, String rpgKnowledge) {
		return new UserDTO(
			"username",
			"mail@example2.com",
			firstName,
			lastName,
			password,
			introduction,
			rpgKnowledge,
			LocalDate.of(2025, 1, 1)
		);
	}

	public static List<QuestDTO> createQuestDTOs() {
		QuestDTO quest1 = new QuestDTO("username", "campagne-1", "main", "quest 1", "description", List.of(new GoalDTO("goal", false)));
		quest1.setId("idQuest1");

		QuestDTO quest2 = new QuestDTO("username", "campagne-2", "main", "quest 2", "description", List.of(new GoalDTO("goal", false)));
		quest2.setId("idQuest2");

		QuestDTO quest3 = new QuestDTO("username", "campagne-3", "main", "quest 3", "description", List.of(new GoalDTO("goal", false)));
		quest3.setId("idQuest3");

		return List.of(quest1, quest2, quest3);
	}

	public static QuestDTO createQuestDTO() {
		return new QuestDTO("alvin", "my-campaign", "main", "title", "description", List.of(new GoalDTO("goal", false)));
	}

	public static List<CampaignDTO> createCampaignDTOs() {
		CampaignDTO campaign1 = new CampaignDTO("username", "campagne 1", "campagne-1", null, null, null, LocalDate.of(2025, 1, 1));
		campaign1.setId("id1");

		CampaignDTO campaign2 = new CampaignDTO("username", "campagne 2", "campagne-2", null, null, null, LocalDate.of(2025, 1, 1));
		campaign2.setId("id2");

		CampaignDTO campaign3 = new CampaignDTO("username", "campagne 3", "campagne-3", null, null, null, LocalDate.of(2025, 1, 1));
		campaign3.setId("id3");

		return List.of(campaign1, campaign2, campaign3);
	}

	public static CampaignDTO createCampaignDTO() {
		CampaignDTO campaignDTO = new CampaignDTO("alvin", "My campaign", "my-campaign", "description", "type", "mood", LocalDate.of(2025, 1, 1));
		campaignDTO.setId("newId");

		return campaignDTO;
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
			new CharacterDTO("username", "campagne-1", "character 1"),
			new CharacterDTO("username", "campagne-2", "character 2"),
			new CharacterDTO("username", "campagne-3", "character 3")
		);
	}

	public static CharacterDTO createCharacterDTO() {
		return new CharacterDTO("alvin", "my-campaign", "character 1");
	}

}
