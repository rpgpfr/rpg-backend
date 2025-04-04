package com.rpgproject.domain.usecase.quest;

import com.rpgproject.domain.entity.Quest;
import com.rpgproject.domain.exception.quest.QuestEditFailedException;
import com.rpgproject.domain.port.Presenter;
import com.rpgproject.domain.port.QuestRepository;

public class EditMainQuest<T> {

	private final QuestRepository questRepository;
	private final Presenter<Quest, T> presenter;

	public EditMainQuest(QuestRepository questRepository, Presenter<Quest, T> presenter) {
		this.questRepository = questRepository;
		this.presenter = presenter;
	}

	public T execute(Quest quest, String slug, String owner) {
		try {
			questRepository.updateMainQuest(quest, slug, owner);

			return presenter.ok();
		} catch (QuestEditFailedException e) {
			return presenter.error(e);
		}
	}

}
