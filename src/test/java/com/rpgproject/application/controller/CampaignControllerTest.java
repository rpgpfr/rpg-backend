package com.rpgproject.application.controller;

import com.rpgproject.application.dto.requestbody.CampaignRequestBody;
import com.rpgproject.application.dto.requestbody.CampaignUpdateRequestBody;
import com.rpgproject.application.dto.requestbody.GoalUpdateRequestBody;
import com.rpgproject.application.dto.requestbody.QuestUpdateRequestBody;
import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.CampaignViewModel;
import com.rpgproject.application.dto.viewmodel.QuestViewModel;
import com.rpgproject.application.presenter.CampaignRestPresenter;
import com.rpgproject.application.presenter.CampaignsRestPresenter;
import com.rpgproject.application.presenter.QuestRestPresenter;
import com.rpgproject.application.service.ExceptionHTTPStatusService;
import com.rpgproject.infrastructure.dao.CampaignMongoDao;
import com.rpgproject.infrastructure.dao.CharacterMongoDao;
import com.rpgproject.infrastructure.dao.QuestMongoDao;
import com.rpgproject.infrastructure.repository.CampaignMongoRepository;
import com.rpgproject.infrastructure.repository.CharacterMongoRepository;
import com.rpgproject.infrastructure.repository.QuestMongoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.rpgproject.application.DTOCreationTestUtils.createCampaignViewModels;
import static com.rpgproject.application.DTOCreationTestUtils.createFullCampaignViewModels;
import static com.rpgproject.infrastructure.DTOCreationTestUtils.createCampaignDTOs;
import static com.rpgproject.infrastructure.DTOCreationTestUtils.createQuestDTOs;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class CampaignControllerTest {

	@Autowired
	private CampaignController campaignController;

	@Autowired
	private MongoTemplate mongoTemplate;

	@BeforeEach
	void setUp() {
		mongoTemplate.insert(createCampaignDTOs(), "Campaign");
		mongoTemplate.insert(createQuestDTOs(), "Quest");
	}

	@AfterEach
	void tearDown() {
		mongoTemplate.dropCollection("Campaign");
		mongoTemplate.dropCollection("Quest");
	}

	@Test
	@DisplayName("Given an owner, when getting the user's campaigns, then all of its campaigns are returned")
	void givenAnOwner_whenLookingForTheUsersCampaigns_thenAllOfItsCampaignsAreReturned() {
		// Given
		String owner = "username";

		// When
		ResponseEntity<ResponseViewModel<List<CampaignViewModel>>> actualResponseEntity = campaignController.getAllCampaignsByOwner(owner);

		// Then
		ResponseEntity<ResponseViewModel<List<CampaignViewModel>>> expectedResponseEntity = ResponseEntity.ok(new ResponseViewModel<>(createCampaignViewModels(), null));

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

	@Test
	@DisplayName("Given an owner and a campaign request body, when the campaign does not exist, then it is saved")
	void givenAnOwnerAndACampaignRequestBody_whenTheCampaignDoesNotExist_thenItIsSaved() {
		// Given
		String owner = "username";
		CampaignRequestBody campaignRequestBody = new CampaignRequestBody("my campaign");

		// When
		ResponseEntity<ResponseViewModel<CampaignViewModel>> actualResponseEntity = campaignController.createCampaign(owner, campaignRequestBody);

		// Then
		ResponseEntity<ResponseViewModel<CampaignViewModel>> expectedResponseEntity = ResponseEntity.noContent().build();

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

	@Test
	@DisplayName("Given a slug and an owner, when the campaign exists, then it is returned")
	void givenASlugAndAnOwner_whenTheCampaignExists_thenItIsReturned() {
		// Given
		String owner = "username";
		String slug = "campagne-1";

		// When
		ResponseEntity<ResponseViewModel<CampaignViewModel>> actualResponseEntity = campaignController.getCampaign(owner, slug);

		// Then
		ResponseEntity<ResponseViewModel<CampaignViewModel>> expectedResponseEntity = ResponseEntity.ok(
			new ResponseViewModel<>(
				createFullCampaignViewModels().getFirst(),
				null
			)
		);

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

	@Test
	@DisplayName("Given an owner with a slug and a campaign update request body, when the campaign exists, then it is updated")
	void givenAnOwnerWithASlugAndACampaignUpdateRequestBody_whenTheCampaignExists_thenItIsUpdated() {
		// Given
		String owner = "username";
		String slug = "campagne-1";
		CampaignUpdateRequestBody campaignUpdateRequestBody = new CampaignUpdateRequestBody("description", "type", "mood");

		// When
		ResponseEntity<ResponseViewModel<CampaignViewModel>> actualResponseEntity = campaignController.updateCampaign(owner, slug, campaignUpdateRequestBody);

		// Then
		ResponseEntity<ResponseViewModel<CampaignViewModel>> expectedResponseEntity = ResponseEntity.noContent().build();

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

	@Test
	@DisplayName("Given an owner and a slug, when the campaign exists, then it is deleted")
	void givenAnOwnerAndASlug_whenTheCampaignExists_thenItIsDeleted() {
		// Given
		String owner = "username";
		String slug = "campagne-1";

		// When
		ResponseEntity<ResponseViewModel<CampaignViewModel>> actualResponseEntity = campaignController.deleteCampaign(owner, slug);

		// Then
		ResponseEntity<ResponseViewModel<CampaignViewModel>> expectedResponseEntity = ResponseEntity.noContent().build();

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

	@Test
	@DisplayName("Given an owner with a slug and a quest update request body, when the quest exists, then it is updated")
	void givenAnOwnerWithASlugAndAQuestUpdateRequestBody_whenTheQuestExists_thenItIsUpdated() {
		// Given
		String owner = "username";
		String slug = "campagne-1";
		QuestUpdateRequestBody questUpdateRequestBody = new QuestUpdateRequestBody(
			"AAAAAH",
			"main",
			"All work and no play makes Jack a dull boy",
			List.of(
				new GoalUpdateRequestBody("goal 1", false)
			)
		);

		// When
		ResponseEntity<ResponseViewModel<QuestViewModel>> actualResponseEntity = campaignController.updateQuest(owner, slug, questUpdateRequestBody);

		// Then
		ResponseEntity<ResponseViewModel<QuestViewModel>> expectedResponseEntity = ResponseEntity.noContent().build();

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

}