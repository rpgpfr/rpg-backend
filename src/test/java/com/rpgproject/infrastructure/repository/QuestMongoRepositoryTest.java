package com.rpgproject.infrastructure.repository;

import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.rpgproject.domain.entity.Quest;
import com.rpgproject.domain.exception.DuplicateException;
import com.rpgproject.domain.exception.InternalException;
import com.rpgproject.domain.exception.NotFoundException;
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
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
	void setUp() {
		QuestMongoDao questMongoDao = new QuestMongoDao(mongoTemplate);
		questMongoRepository = new QuestMongoRepository(questMongoDao);

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
					Indexes.ascending("owner"),
					Indexes.ascending("campaignSlug"),
					Indexes.ascending("title")
				),
				indexOptions
			);

		mongoTemplate.insert(createCampaignDTOs(), "Campaign");
		mongoTemplate.insert(createQuestDTOs(), "Quest");
	}

	@AfterEach
	void tearDown() {
		mongoTemplate.dropCollection("Quest");
		mongoTemplate.dropCollection("Campaign");
	}

	@Test
	@DisplayName("Given a campaign slug and an owner, when the main quest exists, then it is returned")
	void givenACampaignSlugAndAnOwner_whenTheMainQuestExists_thenItIsReturned() {
		// Given
		CampaignDTO campaignDTO = createCampaignDTOs().getFirst();

		// When
		Quest actualQuest = questMongoRepository.findMainQuestByCampaignSlugAndOwner(campaignDTO.getSlug(), campaignDTO.getOwner());

		// Then
		Quest expectedQuest = createQuests().getFirst();

		assertThat(actualQuest).isEqualTo(expectedQuest);
	}

	@Test
	@DisplayName("Given a campaign slug and an owner, when the main quest is not found, then an exception is thrown")
	void givenACampaignSlugAndAnOwner_whenTheMainQuestIsNotFound_thenAnExceptionIsThrown() {
		// Given & When & Then
		assertThatCode(() -> questMongoRepository.findMainQuestByCampaignSlugAndOwner(null, null)).isInstanceOf(NotFoundException.class);
	}

	@Test
	@DisplayName("Given a quest, when it does not exist, then it is saved")
	void givenAQuest_whenItDoesNotExist_thenItIsSaved() {
		// Given
		CampaignDTO campaignDTO = createCampaignDTO();
		Quest quest = createQuest();

		mongoTemplate.insert(campaignDTO, "Campaign");

		// When & Then
		assertThatCode(() -> questMongoRepository.save(quest)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a quest, when save fails because it already exists, then an exception is thrown")
	void givenAQuest_whenSaveFailsBecauseItAlreadyExists_thenAnExceptionIsThrown() {
		// Given
		Quest quest = createQuests().getFirst();

		// When & Then
		assertThatCode(() -> questMongoRepository.save(quest)).isInstanceOf(DuplicateException.class);
	}

	@Test
	@DisplayName("Given a quest, when save fails, then an exception is thrown")
	void givenAQuest_whenSaveFails_thenAnExceptionIsThrown() {
		// Given
		Quest quest = createQuests().getFirst();
		MongoTemplate mongoTemplateMock = mock(MongoTemplate.class);
		QuestMongoDao questMongoDao = new QuestMongoDao(mongoTemplateMock);

		ReflectionTestUtils.setField(questMongoRepository, "questMongoDao", questMongoDao);

		when(mongoTemplateMock.insert(any(QuestDTO.class))).thenThrow(RuntimeException.class);

		// When & Then
		assertThatCode(() -> questMongoRepository.save(quest)).isInstanceOf(InternalException.class);
	}

	@Test
	@DisplayName("Given a main quest, when it exists, then it is updated")
	void givenAMainQuest_whenItExists_thenItIsUpdated() {
		// Given
		CampaignDTO campaignDTO = createCampaignDTO();
		QuestDTO questDTO = createQuestDTO();
		questDTO.setTitle("Old title");

		mongoTemplate.insert(campaignDTO, "Campaign");
		mongoTemplate.insert(questDTO, "Quest");
		Quest quest = createQuest();

		// When & Then
		assertThatCode(() -> questMongoRepository.updateMainQuest(quest)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a main quest, when update fails because it is not found, then an exception is thrown")
	void givenAMainQuest_whenUpdateFailsBecauseItIsNotFound_thenAnExceptionIsThrown() {
		// Given
		Quest quest = createQuest();

		// When & Then
		assertThatCode(() -> questMongoRepository.updateMainQuest(quest)).isInstanceOf(NotFoundException.class);
	}

	@Test
	@DisplayName("Given a main quest, when update fails, then an exception is thrown")
	void givenAMainQuest_whenUpdateFails_thenAnExceptionIsThrown() {
		// Given
		Quest quest = createQuest();
		MongoTemplate mongoTemplateMock = mock(MongoTemplate.class);
		QuestMongoDao questMongoDao = new QuestMongoDao(mongoTemplateMock);

		ReflectionTestUtils.setField(questMongoRepository, "questMongoDao", questMongoDao);

		when(mongoTemplateMock.findAndModify(any(Query.class), any(Update.class), eq(QuestDTO.class))).thenThrow(RuntimeException.class);

		// When & Then
		assertThatCode(() -> questMongoRepository.updateMainQuest(quest)).isInstanceOf(InternalException.class);
	}

	@Test
	@DisplayName("Should delete all quests related to campaign slug and owner")
	void shouldDeleteAllQuestsRelatedToCampaignSlugAndOwner() {
		// Given
		CampaignDTO campaignDTO = createCampaignDTOs().getFirst();
		String slug = campaignDTO.getSlug();
		String owner = campaignDTO.getOwner();

		// When & Then
		assertThatCode(() -> questMongoRepository.deleteAllByCampaignSlugAndOwner(slug, owner)).doesNotThrowAnyException();
	}

}