package com.rpgproject.infrastructure.repository;

import com.rpgproject.domain.entity.Quest;
import com.rpgproject.infrastructure.dao.CampaignMongoDao;
import com.rpgproject.infrastructure.dao.QuestMongoDao;
import org.springframework.stereotype.Repository;

@Repository
public class QuestMongoRepository {

	private final QuestMongoDao questMongoDao;
	private final CampaignMongoDao campaignMongoDao;

	public QuestMongoRepository(QuestMongoDao questMongoDao, CampaignMongoDao campaignMongoDao) {
		this.questMongoDao = questMongoDao;
		this.campaignMongoDao = campaignMongoDao;
	}

	public Quest findMainQuestByOwnerAndSlug(String owner, String slug) {
		return null;
	}

	public void editMainQuest(Quest quest, String owner, String slug) {

	}

}
