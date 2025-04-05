package com.rpgproject.application.presenter;

import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.CampaignViewModel;
import com.rpgproject.domain.entity.Campaign;
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

	private CampaignRestPresenter presenter;

	@BeforeEach
	public void setUp() {
		presenter = new CampaignRestPresenter();
	}

	@Test
	@DisplayName("Should return an empty response entity")
	void shouldReturnAnEmptyResponseEntity() {
		// Act
		ResponseEntity<ResponseViewModel<CampaignViewModel>> actualResponseEntity = presenter.ok();

		// Assert
		ResponseEntity<ResponseViewModel<CampaignViewModel>> expectedResponseEntity = ResponseEntity.ok().build();

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

	@Test
	@DisplayName("Given a campaign, when presenting it, then return a response entity containing the campaigns view model")
	void givenACampaign_whenPresentingIt_thenReturnAResponseEntityContainingTheCampaignsViewModel() {
		// Given
		Campaign campaign = createCampaign();

		// When
		ResponseEntity<ResponseViewModel<CampaignViewModel>> actualResponseEntity = presenter.ok(campaign);

		// Then
		ResponseEntity<ResponseViewModel<CampaignViewModel>> expectedResponseEntity = ResponseEntity.ok(new ResponseViewModel<>(createCampaignViewModel(), null));

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

	@Test
	@DisplayName("Given a campaign with main quest, when presenting it, then return a response entity containing the campaigns view model")
	void givenACampaignWithMainQuest_whenPresentingIt_thenReturnAResponseEntityContainingTheCampaignsViewModel() {
		// Given
		Campaign campaign = createFullCampaign();

		// When
		ResponseEntity<ResponseViewModel<CampaignViewModel>> actualResponseEntity = presenter.ok(campaign);

		// Then
		ResponseEntity<ResponseViewModel<CampaignViewModel>> expectedResponseEntity = ResponseEntity.ok(new ResponseViewModel<>(createFullCampaignViewModel(), null));

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

	@Test
	@DisplayName("Should return a response with an error message")
	void shouldReturnAResponseWithAnErrorMessage() {
		// Arrange
		Exception exception = new Exception("error message");

		// Act
		ResponseEntity<ResponseViewModel<CampaignViewModel>> actualResponseEntity = presenter.error(exception);

		// Assert
		ResponseEntity<ResponseViewModel<CampaignViewModel>> expectedResponseEntity = ResponseEntity.badRequest().body(new ResponseViewModel<>(null, "error message"));

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

}