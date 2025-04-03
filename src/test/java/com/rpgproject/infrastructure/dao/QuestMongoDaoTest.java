package com.rpgproject.infrastructure.dao;

import com.rpgproject.infrastructure.dto.QuestDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import static com.rpgproject.infrastructure.DTOCreationTestUtils.createQuestDTO;
import static com.rpgproject.infrastructure.DTOCreationTestUtils.createQuestDTOs;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DataMongoTest
@ActiveProfiles("test")
class QuestMongoDaoTest {

	private QuestMongoDao questMongoDao;

	@Autowired
	private MongoTemplate mongoTemplate;

	@BeforeEach
	public void setUp() {
		questMongoDao = new QuestMongoDao(mongoTemplate);
		mongoTemplate.insert(createQuestDTOs(), "Quest");
	}

	@AfterEach
	public void tearDown() {
		mongoTemplate.dropCollection("Quest");
	}

	@Test
	@DisplayName("Given a campaignId, when looking for the campaign's main quest, then its main quest is returned")
	void givenACampaignId_whenLookingForTheCampaignsMainQuest_thenItsMainQuestIsReturned() {
		// Given
		String campaignId = "id1";

		// When
		QuestDTO actualQuest = questMongoDao.findMainQuestByCampaignId(campaignId);

		// Then
		QuestDTO expectedQuest = createQuestDTOs().getFirst();

		assertThat(actualQuest).isEqualTo(expectedQuest);
	}

	@Test
	@DisplayName("Given a questDTO, when saving it, then it is saved")
	void givenAQuestDTO_whenSavingIt_thenItIsSaved() {
		// Given
		QuestDTO questDTO = createQuestDTO();

		// When
		questMongoDao.save(questDTO);

		// Then
		QuestDTO actualQuest = questMongoDao.findMainQuestByCampaignId(questDTO.getCampaignId());
		QuestDTO expectedQuest = createQuestDTO();

		assertThat(actualQuest).isEqualTo(expectedQuest);
	}

	@Test
	@DisplayName("Given a questDTO, when saving fails, then an exception is thrown")
	void givenAQuestDTO_whenSavingFails_thenAnExceptionIsThrown() {
		// Given & When & Then
		assertThatCode(() -> questMongoDao.save(null)).isInstanceOf(RuntimeException.class);
	}

	@Test
	@DisplayName("Given an existing questDTO, when editing it, then it is updated")
	void givenAnExistingQuestDTO_whenEditingIt_thenItIsUpdated() {
		// Given
		QuestDTO questDTO = createQuestDTOs().getFirst();
		questDTO.setTitle("updated title");

		// When
		questMongoDao.updateMainQuest(questDTO);

		// Then
		QuestDTO actualQuest = questMongoDao.findMainQuestByCampaignId(questDTO.getCampaignId());
		QuestDTO expectedQuest = createQuestDTOs().getFirst();
		expectedQuest.setTitle("updated title");

		assertThat(actualQuest).isEqualTo(expectedQuest);
	}

	@Test
	@DisplayName("Given a questDTO with wrong campaignId, when updating, then an exception is thrown")
	void givenAQuestDTOWithWrongCampaignId_whenUpdating_thenAnExceptionIsThrown() {
		// Given
		QuestDTO questDTO = createQuestDTO();
		questDTO.setCampaignId("wrongCampaignId");

		// When & Then
		assertThatCode(() -> questMongoDao.updateMainQuest(questDTO)).isInstanceOf(RuntimeException.class);
	}

	@Test
	@DisplayName("Given a questDTO, when updating fails, then an exception is thrown")
	void givenAQuestDTO_whenUpdatingFails_thenAnExceptionIsThrown() {
		// Given & When & Then
		assertThatCode(() -> questMongoDao.updateMainQuest(null)).isInstanceOf(RuntimeException.class);
	}

}