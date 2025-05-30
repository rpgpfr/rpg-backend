package com.rpgproject.domain.usecase.quest;

import com.rpgproject.domain.entity.Quest;
import com.rpgproject.domain.exception.InternalException;
import com.rpgproject.domain.port.Presenter;
import com.rpgproject.domain.port.QuestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.rpgproject.domain.EntityCreationTestUtils.createQuest;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateMainQuestTest {

	private UpdateMainQuest<?> updateMainQuest;

	@Mock
	private QuestRepository questRepository;

	@Mock
	private Presenter<Quest, ?> presenter;

	@BeforeEach
	void setUp() {
		this.updateMainQuest = new UpdateMainQuest<>(questRepository, presenter);
	}

	@Test
	@DisplayName("Given a quest, when updating it, then a success is presented")
	void givenAQuest_whenEditingIt_thenASuccessIsPresented() {
		// Given
		Quest quest = createQuest();

		// When
		updateMainQuest.execute(quest);

		// Then
		verify(questRepository, times(1)).updateMainQuest(quest);
		verify(presenter, times(1)).ok();
	}

	@Test
	@DisplayName("Given a quest, when update fails, then an exception is presented")
	void givenAQuest_whenUpdateFails_thenAnExceptionIsPresented() {
		// Given
		Quest quest = createQuest();
		InternalException exception = new InternalException("error");

		doThrow(exception).when(questRepository).updateMainQuest(quest);

		// When
		updateMainQuest.execute(quest);

		// Then
		verify(questRepository, times(1)).updateMainQuest(quest);
		verify(presenter, times(1)).error(exception);
	}

}