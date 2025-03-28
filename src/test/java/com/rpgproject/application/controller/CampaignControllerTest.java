package com.rpgproject.application.controller;

import com.rpgproject.application.dto.requestbody.CampaignRequestBody;
import com.rpgproject.application.dto.requestbody.CampaignUpdateRequestBody;
import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.CampaignViewModel;
import com.rpgproject.application.presenter.CampaignRestPresenter;
import com.rpgproject.application.presenter.CampaignsRestPresenter;
import com.rpgproject.infrastructure.dao.CampaignMongoDao;
import com.rpgproject.infrastructure.repository.CampaignMongoRepository;
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

import static com.rpgproject.utils.CreationTestUtils.createCampaignDTOs;
import static com.rpgproject.utils.CreationTestUtils.createCampaignViewModels;
import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ActiveProfiles("test")
@Import({
	CampaignController.class,
	CampaignsRestPresenter.class,
	CampaignRestPresenter.class,
	CampaignMongoRepository.class,
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
	}

	@AfterEach
	public void tearDown() {
		mongoTemplate.dropCollection("Campaign");
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
				new CampaignViewModel("my campaign", "my-campaign", null, null, null),
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
		String slug = "my-campaign";
		CampaignUpdateRequestBody campaignUpdateRequestBody = new CampaignUpdateRequestBody("my updated campaign", "description", "type", "mood");

		// When
		ResponseEntity<ResponseViewModel<CampaignViewModel>> actualResponseEntity = campaignController.updateCampaign(owner, slug, campaignUpdateRequestBody);

		// Then
		ResponseEntity<ResponseViewModel<CampaignViewModel>> expectedResponseEntity = ResponseEntity.ok(
			new ResponseViewModel<>(
				new CampaignViewModel(null, "my-updated-campaign", null, null, null),
				null
			)
		);

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

}