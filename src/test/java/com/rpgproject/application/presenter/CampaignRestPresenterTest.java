package com.rpgproject.application.presenter;

import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.CampaignViewModel;
import com.rpgproject.application.service.ExceptionHTTPStatusService;
import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.exception.InternalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static com.rpgproject.application.DTOCreationTestUtils.createCampaignViewModel;
import static com.rpgproject.application.DTOCreationTestUtils.createFullCampaignViewModel;
import static com.rpgproject.domain.EntityCreationTestUtils.createCampaign;
import static com.rpgproject.domain.EntityCreationTestUtils.createFullCampaign;
import static org.assertj.core.api.Assertions.assertThat;

class CampaignRestPresenterTest {

	private CampaignRestPresenter campaignRestPresenter;
	private ExceptionHTTPStatusService exceptionHTTPStatusService;

	@BeforeEach
	void setUp() {
		this.exceptionHTTPStatusService = new ExceptionHTTPStatusService();
		this.campaignRestPresenter = new CampaignRestPresenter(exceptionHTTPStatusService);
	}

	@Test
	@DisplayName("Should return an empty response")
	void shouldReturnAnEmptyResponse() {
		// Act
		ResponseEntity<ResponseViewModel<CampaignViewModel>> actualResponseEntity = campaignRestPresenter.ok();

		// Assert
		ResponseEntity<ResponseViewModel<CampaignViewModel>> expectedResponseEntity = ResponseEntity.noContent().build();

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

	@Test
	@DisplayName("Given a campaign, when presenting it, then a response containing the campaign is returned")
	void givenACampaign_whenPresentingIt_thenAResponseContainingTheCampaignIsReturned() {
		// Given
		Campaign campaign = createCampaign();

		// When
		ResponseEntity<ResponseViewModel<CampaignViewModel>> actualResponseEntity = campaignRestPresenter.ok(campaign);

		// Then
		ResponseEntity<ResponseViewModel<CampaignViewModel>> expectedResponseEntity = ResponseEntity.ok(new ResponseViewModel<>(createCampaignViewModel(), null));

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

	@Test
	@DisplayName("Given a campaign with main quest, when presenting it, then a response entity containing the campaign is returned")
	void givenACampaignWithMainQuest_whenPresentingIt_thenAResponseContainingTheCampaignIsReturned() {
		// Given
		Campaign campaign = createFullCampaign();

		// When
		ResponseEntity<ResponseViewModel<CampaignViewModel>> actualResponseEntity = campaignRestPresenter.ok(campaign);

		// Then
		ResponseEntity<ResponseViewModel<CampaignViewModel>> expectedResponseEntity = ResponseEntity.ok(new ResponseViewModel<>(createFullCampaignViewModel(), null));

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

	@Test
	@DisplayName("Should return a response with an error message")
	void shouldReturnAResponseWithAnErrorMessage() {
		// Arrange
		InternalException exception = new InternalException("error");

		// Act
		ResponseEntity<ResponseViewModel<CampaignViewModel>> actualResponseEntity = campaignRestPresenter.error(exception);

		// Assert
		ResponseEntity<ResponseViewModel<CampaignViewModel>> expectedResponseEntity = ResponseEntity.internalServerError().body(new ResponseViewModel<>(null, "error"));

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

}