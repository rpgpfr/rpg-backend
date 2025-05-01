package com.rpgproject.application.presenter;

import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.QuestViewModel;
import com.rpgproject.application.service.ExceptionHTTPStatusService;
import com.rpgproject.domain.entity.Quest;
import com.rpgproject.domain.exception.InternalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static com.rpgproject.application.DTOCreationTestUtils.createQuestViewModel;
import static com.rpgproject.domain.EntityCreationTestUtils.createQuest;
import static org.assertj.core.api.Assertions.assertThat;

class QuestRestPresenterTest {

	private QuestRestPresenter questRestPresenter;
	private ExceptionHTTPStatusService exceptionHTTPStatusService;

	@BeforeEach
	void setUp() {
		this.exceptionHTTPStatusService = new ExceptionHTTPStatusService();
		this.questRestPresenter = new QuestRestPresenter(exceptionHTTPStatusService);
	}

	@Test
	@DisplayName("Should return an empty response")
	void shouldReturnAnEmptyResponse() {
		// Act
		ResponseEntity<ResponseViewModel<QuestViewModel>> actualResponseEntity = questRestPresenter.ok();

		// Assert
		ResponseEntity<ResponseViewModel<QuestViewModel>> expectedResponseEntity = ResponseEntity.ok().build();

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

	@Test
	@DisplayName("Should return a response with a quest")
	void shouldReturnAResponseWithAQuest() {
		// Arrange
		Quest quest = createQuest();

		// Act
		ResponseEntity<ResponseViewModel<QuestViewModel>> actualResponseEntity = questRestPresenter.ok(quest);

		// Assert
		ResponseEntity<ResponseViewModel<QuestViewModel>> expectedResponseEntity = ResponseEntity.ok(
			new ResponseViewModel<>(createQuestViewModel(), null)
		);

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

	@Test
	@DisplayName("Should return a response with an error message")
	void shouldReturnAResponseWithAnErrorMessage() {
		// Arrange
		InternalException exception = new InternalException("error");

		// Act
		ResponseEntity<ResponseViewModel<QuestViewModel>> actualResponseEntity = questRestPresenter.error(exception);

		// Assert
		ResponseEntity<ResponseViewModel<QuestViewModel>> expectedResponseEntity = ResponseEntity.internalServerError().body(new ResponseViewModel<>(null, "error"));

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

}