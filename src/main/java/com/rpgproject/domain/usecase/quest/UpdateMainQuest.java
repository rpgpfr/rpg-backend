package com.rpgproject.domain.usecase.quest;

import com.rpgproject.domain.entity.Quest;
import com.rpgproject.domain.exception.InternalException;
import com.rpgproject.domain.exception.NotFoundException;
import com.rpgproject.domain.port.Presenter;
import com.rpgproject.domain.port.QuestRepository;

public class UpdateMainQuest<T> {

	private final QuestRepository questRepository;
	private final Presenter<Quest, T> presenter;

	public UpdateMainQuest(QuestRepository questRepository, Presenter<Quest, T> presenter) {
		this.questRepository = questRepository;
		this.presenter = presenter;
	}

	public T execute(Quest quest, String slug, String owner) {
		try {
			questRepository.updateMainQuest(quest, slug, owner);

			return presenter.ok();
		} catch (NotFoundException | InternalException e) {
			return presenter.error(e);
		}
	}

}
