package com.rpgproject.infrastructure.repository;

import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.rpgproject.domain.entity.Quest;
import com.rpgproject.domain.exception.DuplicateException;
import com.rpgproject.domain.exception.InternalException;
import com.rpgproject.domain.exception.NotFoundException;
import com.rpgproject.infrastructure.dao.CampaignMongoDao;
import com.rpgproject.infrastructure.dao.QuestMongoDao;
import com.rpgproject.infrastructure.dto.CampaignDTO;
import com.rpgproject.infrastructure.dto.QuestDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import static com.rpgproject.domain.EntityCreationTestUtils.createQuest;
import static com.rpgproject.domain.EntityCreationTestUtils.createQuests;
import static com.rpgproject.infrastructure.DTOCreationTestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

@DataMongoTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class QuestMongoRepositoryTest {

	private QuestMongoRepository questMongoRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@BeforeEach
	public void setUp() {
		QuestMongoDao questMongoDao = new QuestMongoDao(mongoTemplate);
		CampaignMongoDao campaignMongoDao = new CampaignMongoDao(mongoTemplate);
		questMongoRepository = new QuestMongoRepository(questMongoDao, campaignMongoDao);

		initializeMongoDB();
	}

	private void initializeMongoDB() {
		IndexOptions indexOptions = new IndexOptions().unique(true);

		mongoTemplate.createCollection("Campaign")
			.createIndex(
				Indexes.compoundIndex(
					Indexes.ascending("slug"),
					Indexes.ascending("owner")
				),
				indexOptions
			);

		mongoTemplate
			.createCollection("Quest")
			.createIndex(
				Indexes.compoundIndex(
					Indexes.ascending("campaignId"),
					Indexes.ascending("title")
				),
				indexOptions
			);

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
	@DisplayName("Given an owner and a slug, when the main quest is not found, then an exception is thrown")
	void givenAnOwnerAndASlug_whenTheMainQuestIsNotFound_thenAnExceptionIsThrown() {
		// Given & When & Then
		assertThatCode(() -> questMongoRepository.findMainQuestBySlugAndOwner(null, null)).isInstanceOf(NotFoundException.class);
	}

	@Test
	@DisplayName("Given a quest with a slug and an owner, when it does not exist, then it is saved")
	void givenAQuestWithASlugAndAnOwner_whenItDoesNotExist_thenItIsSaved() {
		CampaignDTO campaignDTO = createCampaignDTO();
		Quest quest = createQuest();

		mongoTemplate.insert(campaignDTO, "Campaign");

		String owner = campaignDTO.getOwner();
		String slug = campaignDTO.getSlug();

		// When & Then
		assertThatCode(() -> questMongoRepository.save(quest, slug, owner)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a quest with an owner and a campaign slug, when save fails because it already exists, then an exception is thrown")
	void givenAQuestWithAnOwnerAndACampaignSlug_whenSaveFailsBecauseItDoesNotExist_thenAnExceptionIsThrown() {
		// Given
		CampaignDTO campaignDTO = createCampaignDTOs().getFirst();
		Quest quest = createQuests().getFirst();
		String owner = campaignDTO.getOwner();
		String slug = campaignDTO.getSlug();

		// When & Then
		assertThatCode(() -> questMongoRepository.save(quest, slug, owner)).isInstanceOf(DuplicateException.class);
	}

	@Test
	@DisplayName("Given a quest with an owner and a campaign slug, when save fails, then an exception is thrown")
	void givenAQuestWithAnOwnerAndACampaignSlug_whenSaveFails_thenAnExceptionIsThrown() {
		// Given
		CampaignDTO campaignDTO = createCampaignDTO();
		Quest quest = createQuest();
		String owner = campaignDTO.getOwner();
		String slug = "wrong slug";

		// When & Then
		assertThatCode(() -> questMongoRepository.save(quest, slug, owner)).isInstanceOf(InternalException.class);
	}

	@Test
	@DisplayName("Given a main quest with owner and a campaign slug, when it exists, then it is updated")
	void givenAMainQuestWithOwnerAndACampaignSlug_whenItExists_thenItIsUpdated() {
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
	@DisplayName("Given a main quest with an owner and a campaign slug, when update fails because it is not found, then an exception is thrown")
	void givenAMainQuestWithAnOwnerAndACampaignSlug_whenUpdateFailsBecauseItIsNotFound_thenAnExceptionIsThrown() {
		// Given
		String owner = "wrong owner";
		String slug = "wrong slug";

		// When & Then
		assertThatCode(() -> questMongoRepository.updateMainQuest(null, slug, owner)).isInstanceOf(NotFoundException.class);
	}

	@Test
	@DisplayName("Given a main quest with an owner and a campaign slug, when update fails, then an exception is thrown")
	void givenAMainQuestWithAnOwnerAndACampaignSlug_whenUpdateFails_thenAnExceptionIsThrown() {
		// Given
		CampaignMongoDao campaignMongoDaoMock = mock(CampaignMongoDao.class);
		QuestMongoDao questMongoDaoMock = mock(QuestMongoDao.class);

		ReflectionTestUtils.setField(questMongoRepository, "campaignMongoDao", campaignMongoDaoMock);
		ReflectionTestUtils.setField(questMongoRepository, "questMongoDao", questMongoDaoMock);

		when(campaignMongoDaoMock.findCampaignIdBySlugAndOwner(null, null)).thenReturn("id");
		doThrow(new RuntimeException()).when(questMongoDaoMock).updateMainQuest(any(QuestDTO.class));

		// When & Then
		assertThatCode(() -> questMongoRepository.updateMainQuest(createQuest(), null, null)).isInstanceOf(InternalException.class);
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