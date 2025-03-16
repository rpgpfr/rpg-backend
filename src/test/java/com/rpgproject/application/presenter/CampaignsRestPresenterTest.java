package com.rpgproject.application.presenter;

import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.CampaignViewModel;
import com.rpgproject.domain.entity.Campaign;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.rpgproject.utils.CreationTestUtils.createCampaignViewModels;
import static com.rpgproject.utils.CreationTestUtils.createCampaigns;
import static org.assertj.core.api.Assertions.assertThat;

class CampaignsRestPresenterTest {

	private CampaignsRestPresenter campaignsRestPresenter;

	@BeforeEach
	public void setUp() {
		campaignsRestPresenter = new CampaignsRestPresenter();
	}

	@Test
	@DisplayName("Should return an empty response entity")
	void shouldReturnAnEmptyResponseEntity() {
		// Act
		ResponseEntity<ResponseViewModel<List<CampaignViewModel>>> actualResponseEntity = campaignsRestPresenter.ok();

		// Assert
		ResponseEntity<ResponseViewModel<List<CampaignViewModel>>> expectedResponseEntity = ResponseEntity.ok().build();

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

	@Test
	@DisplayName("Given a campaign list, when presenting them, then return a response entity containing the campaigns view models")
	void givenACampaignList_whenPresentingThem_thenReturnAResponseEntityContainingTheCampaignsViewModels() {
		// Given
		List<Campaign> campaigns = createCampaigns();

		// When
		ResponseEntity<ResponseViewModel<List<CampaignViewModel>>> actualResponseEntity = campaignsRestPresenter.ok(campaigns);

		// Then
		ResponseEntity<ResponseViewModel<List<CampaignViewModel>>> expectedResponseEntity = ResponseEntity.ok(new ResponseViewModel<>(createCampaignViewModels(), null));

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

	@Test
	@DisplayName("Should return a response with an error message")
	void shouldReturnAResponseWithAnErrorMessage() {
		// Arrange
		Exception exception = new Exception("error message");

		// Act
		ResponseEntity<ResponseViewModel<List<CampaignViewModel>>> actualResponseEntity = campaignsRestPresenter.error(exception);

		// Assert
		ResponseEntity<ResponseViewModel<List<CampaignViewModel>>> expectedResponseEntity = ResponseEntity.badRequest().body(new ResponseViewModel<>(null, "error message"));

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

}