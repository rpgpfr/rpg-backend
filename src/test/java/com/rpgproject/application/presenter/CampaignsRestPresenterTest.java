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

import java.util.List;

import static com.rpgproject.application.DTOCreationTestUtils.createCampaignViewModels;
import static com.rpgproject.domain.EntityCreationTestUtils.createCampaigns;
import static org.assertj.core.api.Assertions.assertThat;

class CampaignsRestPresenterTest {

	private CampaignsRestPresenter campaignsRestPresenter;
	private ExceptionHTTPStatusService exceptionHTTPStatusService;

	@BeforeEach
	void setUp() {
		this.exceptionHTTPStatusService = new ExceptionHTTPStatusService();
		this.campaignsRestPresenter = new CampaignsRestPresenter(exceptionHTTPStatusService);
	}

	@Test
	@DisplayName("Should return an empty response")
	void shouldReturnAnEmptyResponse() {
		// Act
		ResponseEntity<ResponseViewModel<List<CampaignViewModel>>> actualResponseEntity = campaignsRestPresenter.ok();

		// Assert
		ResponseEntity<ResponseViewModel<List<CampaignViewModel>>> expectedResponseEntity = ResponseEntity.noContent().build();

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

	@Test
	@DisplayName("should return a response with campaigns")
	void shouldReturnAResponseWithCampaigns() {
		// Arrange
		List<Campaign> campaigns = createCampaigns();

		// Act
		ResponseEntity<ResponseViewModel<List<CampaignViewModel>>> actualResponseEntity = campaignsRestPresenter.ok(campaigns);

		// Assert
		ResponseEntity<ResponseViewModel<List<CampaignViewModel>>> expectedResponseEntity = ResponseEntity.ok(new ResponseViewModel<>(createCampaignViewModels(), null));

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

	@Test
	@DisplayName("Should return a response with an error message")
	void shouldReturnAResponseWithAnErrorMessage() {
		// Arrange
		InternalException exception = new InternalException("error");

		// Act
		ResponseEntity<ResponseViewModel<List<CampaignViewModel>>> actualResponseEntity = campaignsRestPresenter.error(exception);

		// Assert
		ResponseEntity<ResponseViewModel<List<CampaignViewModel>>> expectedResponseEntity = ResponseEntity.internalServerError().body(new ResponseViewModel<>(null, "error"));

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

}