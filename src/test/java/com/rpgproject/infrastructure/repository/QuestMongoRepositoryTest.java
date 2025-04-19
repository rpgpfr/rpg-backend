package com.rpgproject.infrastructure.repository;

import com.rpgproject.domain.entity.Quest;
import com.rpgproject.domain.exception.quest.MainQuestNotFoundException;
import com.rpgproject.domain.exception.quest.QuestCreationException;
import com.rpgproject.domain.exception.quest.QuestEditFailedException;
import com.rpgproject.infrastructure.dao.CampaignMongoDao;
import com.rpgproject.infrastructure.dao.QuestMongoDao;
import com.rpgproject.infrastructure.dto.CampaignDTO;
import com.rpgproject.infrastructure.dto.QuestDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import static com.rpgproject.domain.EntityCreationTestUtils.createQuest;
import static com.rpgproject.domain.EntityCreationTestUtils.createQuests;
import static com.rpgproject.infrastructure.DTOCreationTestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DataMongoTest
@ActiveProfiles("test")
class QuestMongoRepositoryTest {

	private QuestMongoRepository questMongoRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@BeforeEach
	public void setUp() {
		QuestMongoDao questMongoDao = new QuestMongoDao(mongoTemplate);
		CampaignMongoDao campaignMongoDao = new CampaignMongoDao(mongoTemplate);
		questMongoRepository = new QuestMongoRepository(questMongoDao, campaignMongoDao);

		mongoTemplate.insert(createCampaignDTOs(), "Campaign");
		mongoTemplate.insert(createQuestDTOs(), "Quest");
	}

	@AfterEach
	public void tearDown() {
		mongoTemplate.dropCollection("Quest");
		mongoTemplate.dropCollection("Campaign");
	}

	@Test
	@DisplayName("Given an owner and a slug, when the main quest exists, then it is returned")
	void givenAnOwnerAndASlug_whenTheMainQuestExists_thenItIsReturned() {
		// Given
		CampaignDTO campaignDTO = createCampaignDTOs().getFirst();

		// When
		Quest actualQuest = questMongoRepository.findMainQuestBySlugAndOwner(campaignDTO.getSlug(), campaignDTO.getOwner());

		// Then
		Quest expectedQuest = createQuests().getFirst();

		assertThat(actualQuest).isEqualTo(expectedQuest);
	}

	@Test
	@DisplayName("Given an owner and a slug, when the main quest does not exist, then an exception is thrown")
	void givenAnOwnerAndASlug_whenTheMainQuestDoesNotExist_thenAnExceptionIsThrown() {
		// Given & When & Then
		assertThatCode(() -> questMongoRepository.findMainQuestBySlugAndOwner(null, null)).isInstanceOf(MainQuestNotFoundException.class);
	}

	@Test
	@DisplayName("Given a quest with a slug and an owner, when saving it, then it is saved")
	void givenAQuestWithASlugAndAnOwner_whenSavingAQuest_thenItIsSaved() {
		CampaignDTO campaignDTO = createCampaignDTO();
		Quest quest = createQuest();

		mongoTemplate.insert(campaignDTO, "Campaign");

		String owner = campaignDTO.getOwner();
		String slug = campaignDTO.getSlug();

		// When & Then
		assertThatCode(() -> questMongoRepository.save(quest, slug, owner)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a quest with owner and a wrong campaign slug, when saving it, then an exception is thrown")
	void givenAQuestWithOwnerAndAWrongCampaignSlug_whenSavingIt_thenAnExceptionIsThrown() {
		// Given
		CampaignDTO campaignDTO = createCampaignDTO();
		Quest quest = createQuest();
		String owner = campaignDTO.getOwner();
		String slug = "wrong slug";

		// When & Then
		assertThatCode(() -> questMongoRepository.save(quest, slug, owner)).isInstanceOf(QuestCreationException.class);
	}

	@Test
	@DisplayName("Given a quest with owner and a campaign slug, when editing it, then it is edited")
	void givenAQuestWithOwnerAndACampaignSlug_whenEditingIt_thenItIsEdited() {
		// Given
		CampaignDTO campaignDTO = createCampaignDTO();
		QuestDTO questDTO = createQuestDTO();
		questDTO.setTitle("Old title");

		mongoTemplate.insert(campaignDTO, "Campaign");
		mongoTemplate.insert(questDTO, "Quest");

		String owner = campaignDTO.getOwner();
		String slug = campaignDTO.getSlug();
		Quest quest = createQuest();

		// When & Then
		assertThatCode(() -> questMongoRepository.updateMainQuest(quest, slug, owner)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a quest with owner and a campaign slug, when editing it, then an exception is thrown")
	void givenAQuestWithOwnerAndACampaignSlug_whenEditingIt_thenAnExceptionIsThrown() {
		// Given & When & Then
		assertThatCode(() -> questMongoRepository.updateMainQuest(null, null, null)).isInstanceOf(QuestEditFailedException.class);
	}

	@Test
	@DisplayName("Should delete all quests related to campaignId")
	void shouldDeleteAllQuestsRelatedToCampaignId() {
		// Given
		CampaignDTO campaignDTO = createCampaignDTOs().getFirst();
		String slug = campaignDTO.getSlug();
		String owner = campaignDTO.getOwner();

		// When & Then
		assertThatCode(() -> questMongoRepository.deleteBySlugAndOwner(slug, owner)).doesNotThrowAnyException();
	}

}