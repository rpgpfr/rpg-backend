package com.rpgproject.domain.port;

import com.rpgproject.domain.entity.Quest;

public interface QuestRepository {

	Quest findMainQuestBySlugAndOwner(String slug, String owner);

	void save(Quest quest, String slug, String owner);

	void updateMainQuest(Quest quest, String slug, String owner);

	void deleteBySlugAndOwner(String slug, String owner);

}
