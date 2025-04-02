package com.rpgproject.infrastructure.repository;

import com.rpgproject.domain.entity.Quest;
import com.rpgproject.domain.exception.quest.MainQuestNotFoundException;
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
	@DisplayName("Given an owner and a slug, when finding the main quest, then it is returned")
	void givenAnOwnerAndASlug_whenFindingTheMainQuest_thenItIsReturned() {
		// Given
		CampaignDTO campaignDTO = createCampaignDTOs().getFirst();

		// When
		Quest actualQuest = questMongoRepository.findMainQuestBySlugAndOwner(campaignDTO.getSlug(), campaignDTO.getOwner());

		// Then
		Quest expectedQuest = createQuests().getFirst();

		assertThat(actualQuest).isEqualTo(expectedQuest);
	}

	@Test
	@DisplayName("Given an owner and a slug, when finding the main quest, then an exception is thrown")
	void givenAnOwnerAndASlug_whenFindingTheMainQuest_thenAnExceptionIsThrown() {
		// Given & When & Then
		assertThatCode(() -> questMongoRepository.findMainQuestBySlugAndOwner(null, null)).isInstanceOf(MainQuestNotFoundException.class);
	}

	@Test
	@DisplayName("Given a quest with owner and a campaign slug, when editing it, then it is edited")
	void givenAQuestWithOwnerAndACampaignSlug_whenEditingIt_thenItIsEdited() {
		// Given
		CampaignDTO campaignDTO = createCampaignDTO();
		QuestDTO questDTO = createQuestDTO();
		Quest quest = createQuest();

		mongoTemplate.insert(campaignDTO, "Campaign");
		mongoTemplate.insert(questDTO, "Quest");

		String owner = campaignDTO.getOwner();
		String slug = campaignDTO.getSlug();

		// When
		questMongoRepository.updateMainQuest(quest, slug, owner);

		// Then
		Quest actualQuest = questMongoRepository.findMainQuestBySlugAndOwner(slug, owner);
		Quest expectedQuest = createQuest();

		assertThat(actualQuest).isEqualTo(expectedQuest);
	}

	@Test
	@DisplayName("Given a quest with owner and a campaign slug, when editing it, then an exception is thrown")
	void givenAQuestWithOwnerAndACampaignSlug_whenEditingIt_thenAnExceptionIsThrown() {
		// Given & When & Then
		assertThatCode(() -> questMongoRepository.updateMainQuest(null, null, null)).isInstanceOf(QuestEditFailedException.class);
	}

}