package com.rpgproject.domain.port;

import com.rpgproject.domain.entity.Quest;

public interface QuestRepository {

	Quest findMainQuestByCampaignSlugAndOwner(String slug, String owner);

	void save(Quest quest);

	void updateMainQuest(Quest quest);

	void deleteAllByCampaignSlugAndOwner(String slug, String owner);

}
