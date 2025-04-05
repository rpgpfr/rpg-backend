package com.rpgproject.application.controller;

import com.rpgproject.application.dto.requestbody.CampaignRequestBody;
import com.rpgproject.application.dto.requestbody.CampaignUpdateRequestBody;
import com.rpgproject.application.dto.requestbody.QuestUpdateRequestBody;
import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.CampaignViewModel;
import com.rpgproject.application.dto.viewmodel.QuestViewModel;
import com.rpgproject.application.presenter.CampaignRestPresenter;
import com.rpgproject.application.presenter.CampaignsRestPresenter;
import com.rpgproject.application.presenter.QuestRestPresenter;
import com.rpgproject.infrastructure.dao.CampaignMongoDao;
import com.rpgproject.infrastructure.dao.QuestMongoDao;
import com.rpgproject.infrastructure.repository.CampaignMongoRepository;
import com.rpgproject.infrastructure.repository.QuestMongoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.rpgproject.application.DTOCreationTestUtils.createCampaignViewModels;
import static com.rpgproject.application.DTOCreationTestUtils.createFullCampaignViewModels;
import static com.rpgproject.infrastructure.DTOCreationTestUtils.createCampaignDTOs;
import static com.rpgproject.infrastructure.DTOCreationTestUtils.createQuestDTOs;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ActiveProfiles("test")
@Import({
	CampaignController.class,
	CampaignsRestPresenter.class,
	CampaignRestPresenter.class,
	QuestRestPresenter.class,
	QuestMongoRepository.class,
	CampaignMongoRepository.class,
	QuestMongoDao.class,
	CampaignMongoDao.class
})
class CampaignControllerTest {

	@Autowired
	private CampaignController campaignController;

	@Autowired
	private MongoTemplate mongoTemplate;

	@BeforeEach
	public void setUp() {
		mongoTemplate.insert(createCampaignDTOs(), "Campaign");
		mongoTemplate.insert(createQuestDTOs(), "Quest");
	}

	@AfterEach
	public void tearDown() {
		mongoTemplate.dropCollection("Campaign");
		mongoTemplate.dropCollection("Quest");
	}

	@Test
	@DisplayName("Given an owner, when looking for all the user's campaigns, then all of its campaigns are returned")
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
	@DisplayName("Given an owner and a campaign request body, when creating it, then the campaign is saved")
	void givenAnOwnerAndACampaignRequestBody_whenCreatingIt_thenTheCampaignIsSaved() {
		// Given
		String owner = "username";
		CampaignRequestBody campaignRequestBody = new CampaignRequestBody("my campaign");

		// When
		ResponseEntity<ResponseViewModel<CampaignViewModel>> actualResponseEntity = campaignController.createCampaign(owner, campaignRequestBody);

		// Then
		ResponseEntity<ResponseViewModel<CampaignViewModel>> expectedResponseEntity = ResponseEntity.ok(
			new ResponseViewModel<>(
				new CampaignViewModel("my campaign", "my-campaign", null, null, null, new QuestViewModel("", "main", "", emptyList())),
				null
			)
		);

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

	@Test
	@DisplayName("Given a slug and an owner, when getting a campaign, then it is returned")
	void givenASlugAndAnOwner_whenGettingACampaign_thenItIsReturned() {
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
	@DisplayName("Given an owner with a slug and a campaign update request body, when updating it, then the new slug is returned")
	void givenAnOwnerWithASlugAndACampaignUpdateRequestBody_whenUpdatingIt_thenTheNewSlugIsReturned() {
		// Given
		String owner = "username";
		String slug = "campagne-1";
		CampaignUpdateRequestBody campaignUpdateRequestBody = new CampaignUpdateRequestBody("my updated campaign", "description", "type", "mood");

		// When
		ResponseEntity<ResponseViewModel<CampaignViewModel>> actualResponseEntity = campaignController.updateCampaign(owner, slug, campaignUpdateRequestBody);

		// Then
		ResponseEntity<ResponseViewModel<CampaignViewModel>> expectedResponseEntity = ResponseEntity.ok(
			new ResponseViewModel<>(
				new CampaignViewModel(null, "my-updated-campaign", null, null, null, null),
				null
			)
		);

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

	@Test
	@DisplayName("Given an owner with a slug and a quest update request body, when updating it, then an empty responseEntity is returned")
	void givenAnOwnerWithASlugAndAQuestUpdateRequestBody_whenUpdatingIt_thenAnEmptyResponseEntityIsReturned() {
		// Given
		String owner = "username";
		String slug = "campagne-1";
		QuestUpdateRequestBody questUpdateRequestBody = new QuestUpdateRequestBody(
			"AAAAAH",
			"main",
			"All work and no play makes Jack a dull boy",
			emptyList()
		);

		// When
		ResponseEntity<ResponseViewModel<QuestViewModel>> actualResponseEntity = campaignController.updateQuest(owner, slug, questUpdateRequestBody);

		// Then
		ResponseEntity<ResponseViewModel<QuestViewModel>> expectedResponseEntity = ResponseEntity.ok().build();

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

}