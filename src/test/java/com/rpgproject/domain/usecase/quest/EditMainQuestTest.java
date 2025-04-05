package com.rpgproject.domain.usecase.quest;

import com.rpgproject.domain.entity.Quest;
import com.rpgproject.domain.exception.quest.QuestEditFailedException;
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
class EditMainQuestTest {

	private EditMainQuest<?> editMainQuest;

	@Mock
	private QuestRepository questRepository;

	@Mock
	private Presenter<Quest, ?> presenter;

	@BeforeEach
	public void setUp() {
		this.editMainQuest = new EditMainQuest<>(questRepository, presenter);
	}

	@Test
	@DisplayName("Given a quest with a slug and an owner, when editing it, then it is updated")
	void givenAQuestWithASlugAndAnOwner_whenEditingIt_thenASuccessIsPresented() {
		// Given
		Quest quest = createQuest();
		String slug = "slug";
		String owner = "owner";

		// When
		editMainQuest.execute(quest, slug, owner);

		// Then
		verify(questRepository, times(1)).updateMainQuest(quest, slug, owner);
		verify(presenter, times(1)).ok();
	}

	@Test
	@DisplayName("Given When Then")
	void givenAQuestWithWrongInfo_whenEditingIt_thenAnExceptionIsPresented() {
		// Given
		Quest quest = createQuest();
		String slug = "wrong slug";
		String owner = "wrong owner";
		QuestEditFailedException exception = new QuestEditFailedException();

		doThrow(exception).when(questRepository).updateMainQuest(quest, slug, owner);

		// When
		editMainQuest.execute(quest, slug, owner);

		// Then
		verify(questRepository, times(1)).updateMainQuest(quest, slug, owner);
		verify(presenter, times(1)).error(exception);
	}

}