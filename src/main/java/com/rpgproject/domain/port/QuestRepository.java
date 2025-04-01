package com.rpgproject.domain.port;

import com.rpgproject.domain.entity.Quest;

public interface QuestRepository {

	Quest findMainQuestBySlugAndOwner(String slug, String owner);

	void editMainQuest(Quest quest, String slug, String owner);

}
