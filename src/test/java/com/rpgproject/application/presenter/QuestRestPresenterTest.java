package com.rpgproject.application.presenter;

import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.QuestViewModel;
import com.rpgproject.domain.entity.Quest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static com.rpgproject.application.DTOCreationTestUtils.createQuestViewModel;
import static com.rpgproject.domain.EntityCreationTestUtils.createQuest;
import static org.assertj.core.api.Assertions.assertThat;

class QuestRestPresenterTest {

	private QuestRestPresenter questRestPresenter;

	@BeforeEach
	public void setUp() {
		this.questRestPresenter = new QuestRestPresenter();
	}

	@Test
	@DisplayName("Should return an empty response entity")
	void shouldReturnAnEmptyResponseEntity() {
		// Act
		ResponseEntity<ResponseViewModel<QuestViewModel>> actualResponseEntity = questRestPresenter.ok();

		// Assert
		ResponseEntity<ResponseViewModel<QuestViewModel>> expectedResponseEntity = ResponseEntity.ok().build();

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

	@Test
	@DisplayName("Given a quest, when presenting it, then return a ResponseEntity containing a QuestViewModel")
	void givenAQuest_whenPresentingIt_thenReturnAResponseEntityContainingAQuestViewModel() {
		// Given
		Quest quest = createQuest();

		// When
		ResponseEntity<ResponseViewModel<QuestViewModel>> actualResponseEntity = questRestPresenter.ok(quest);

		// Then
		ResponseEntity<ResponseViewModel<QuestViewModel>> expectedResponseEntity = ResponseEntity.ok(
			new ResponseViewModel<>(createQuestViewModel(), null)
		);

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

	@Test
	@DisplayName("Should return a response with an error message")
	void shouldReturnAResponseWithAnErrorMessage() {
		// Arrange
		Exception exception = new Exception("error message");

		// Act
		ResponseEntity<ResponseViewModel<QuestViewModel>> actualResponseEntity = questRestPresenter.error(exception);

		// Assert
		ResponseEntity<ResponseViewModel<QuestViewModel>> expectedResponseEntity = ResponseEntity.badRequest().body(new ResponseViewModel<>(null, "error message"));

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

}