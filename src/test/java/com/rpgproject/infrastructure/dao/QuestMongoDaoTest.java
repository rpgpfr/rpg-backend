package com.rpgproject.infrastructure.dao;

import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.rpgproject.infrastructure.dto.QuestDTO;
import com.rpgproject.infrastructure.exception.questmongo.DuplicateQuestNameException;
import com.rpgproject.infrastructure.exception.questmongo.QuestNotFoundException;
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
	void setUp() {
		questMongoDao = new QuestMongoDao(mongoTemplate);
		initializeMongoDB();
	}

	private void initializeMongoDB() {
		IndexOptions indexOptions = new IndexOptions().unique(true);

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

		mongoTemplate.insert(createQuestDTOs(), "Quest");
	}

	@AfterEach
	void tearDown() {
		mongoTemplate.dropCollection("Quest");
	}

	@Test
	@DisplayName("Given a campaign slug and an owner, when a main quest exists, then it is returned")
	void givenACampaignSlugAndAnOwner_whenAMainQuestExists_thenItIsReturned() {
		// Given
		QuestDTO questDTO = createQuestDTOs().getFirst();
		String slug = questDTO.getCampaignSlug();
		String owner = questDTO.getOwner();

		// When
		QuestDTO actualQuest = questMongoDao.findMainQuestByCampaignSlugAndOwner(slug, owner);

		// Then
		QuestDTO expectedQuest = createQuestDTOs().getFirst();

		assertThat(actualQuest).isEqualTo(expectedQuest);
	}

	@Test
	@DisplayName("Given a campaign slug and an owner, when a main quest is not found, then an exception is thrown")
	void givenACampaignSlugAndAnOwner_whenAMainQuestIsNotFound_thenAnExceptionIsThrown() {
		// Given
		String campaignSlug = "wrong slug";
		String owner = "wrong owner";

		// When & Then
		assertThatCode(() -> questMongoDao.findMainQuestByCampaignSlugAndOwner(campaignSlug, owner)).isInstanceOf(QuestNotFoundException.class);
	}

	@Test
	@DisplayName("Given a questDTO, when it does not exist, then it is saved")
	void givenAQuestDTO_whenItDoesNotExist_thenItIsSaved() {
		// Given
		QuestDTO questDTO = createQuestDTO();

		// When & Then
		assertThatCode(() -> questMongoDao.save(questDTO)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a questDTO, when save fails because it already exists, then an exception is thrown")
	void givenAQuestDTO_whenSaveFailsBecauseItAlreadyExists_thenAnExceptionIsThrown() {
		// Given
		QuestDTO questDTO = createQuestDTOs().getFirst();

		// When & Then
		assertThatCode(() -> questMongoDao.save(questDTO)).isInstanceOf(DuplicateQuestNameException.class);
	}

	@Test
	@DisplayName("Given a questDTO, when save fails, then an exception is thrown")
	void givenAQuestDTO_whenSaveFails_thenAnExceptionIsThrown() {
		// Given & When & Then
		assertThatCode(() -> questMongoDao.save(null))
			.isInstanceOf(RuntimeException.class)
			.hasMessage("Une erreur est survenue lors de la création de la quête.");
	}

	@Test
	@DisplayName("Given a questDTO, when it exists, then it is updated")
	void givenAQuestDTO_whenItExists_thenItIsUpdated() {
		// Given
		QuestDTO questDTO = createQuestDTOs().getFirst();
		questDTO.setTitle("updated title");

		// When
		assertThatCode(() -> questMongoDao.updateMainQuest(questDTO)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a questDTO, when update fails because it is not found, then an exception is thrown")
	void givenAQuestDTO_whenUpdateFailsBecauseItIsNotFound_thenAnExceptionIsThrown() {
		// Given
		QuestDTO questDTO = createQuestDTO();

		// When & Then
		assertThatCode(() -> questMongoDao.updateMainQuest(questDTO)).isInstanceOf(QuestNotFoundException.class);
	}

	@Test
	@DisplayName("Given a questDTO, when update fails, then an exception is thrown")
	void givenAQuestDTO_whenUpdateFails_thenAnExceptionIsThrown() {
		// Given & When & Then
		assertThatCode(() -> questMongoDao.updateMainQuest(null))
			.isInstanceOf(RuntimeException.class)
			.hasMessage("Une erreur est survenue lors de la mise à jour des informations.");
	}

	@Test
	@DisplayName("Should delete all quests related to campaignSlug and owner")
	void shouldDeleteAllQuestsRelatedToCampaignSlugAndOwner() {
		// Given
		QuestDTO questDTO = createQuestDTOs().getFirst();
		String campaignSlug = questDTO.getCampaignSlug();
		String owner = questDTO.getOwner();

		// When & Then
		assertThatCode(() -> questMongoDao.deleteByCampaignSlugAndOwner(campaignSlug, owner)).doesNotThrowAnyException();
	}

}