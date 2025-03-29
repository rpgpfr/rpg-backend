package com.rpgproject.infrastructure.repository;

import com.rpgproject.domain.entity.Quest;
import com.rpgproject.infrastructure.dao.CampaignMongoDao;
import com.rpgproject.infrastructure.dao.QuestMongoDao;
import com.rpgproject.infrastructure.dto.CampaignDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import static com.rpgproject.domain.EntityCreationTestUtils.createQuest;
import static com.rpgproject.infrastructure.DTOCreationTestUtils.*;

@DataMongoTest
@ActiveProfiles("test")
class QuestMongoRepositoryTest {

	private QuestMongoRepository questMongoRepository;

	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private QuestMongoDao questMongoDao;

	@BeforeEach
	public void setUp() {
		QuestMongoDao questMongoDao = new QuestMongoDao(mongoTemplate);
		CampaignMongoDao campaignMongoDao = new CampaignMongoDao(mongoTemplate);
		questMongoRepository = new QuestMongoRepository(questMongoDao, campaignMongoDao);

		mongoTemplate.insert(createCampaignDTOs(), "Campaign");
		mongoTemplate.insert(createCampaignDTO(), "Campaign");
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
		Quest actualQuest = questMongoRepository.findMainQuestByOwnerAndSlug(campaignDTO.getOwner(), campaignDTO.getSlug());

		// Then
//		Quest expectedQuest = createQuestDTOs().getFirst();
	}

	@Test
	@DisplayName("Given a quest with owner and a campaign slug, when editing it, then it is edited")
	void givenAQuestWithOwnerAndACampaignSlug_whenEditingIt_thenItIsEdited() {
		// Given
		CampaignDTO campaignDTO = createCampaignDTO();
		String owner = campaignDTO.getOwner();
		String slug = campaignDTO.getSlug();
		Quest quest = createQuest();

		// When
		questMongoRepository.editMainQuest(quest, owner, slug);

		// Then
	}

}