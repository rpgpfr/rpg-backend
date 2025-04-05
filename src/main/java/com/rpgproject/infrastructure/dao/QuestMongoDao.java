package com.rpgproject.infrastructure.dao;

import com.rpgproject.infrastructure.dto.QuestDTO;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Component
public class QuestMongoDao {

	private final MongoTemplate mongoTemplate;

	public QuestMongoDao(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	public QuestDTO findMainQuestByCampaignId(String campaignId) {
		Query query = buildMainQuestByCampaignIdQuery(campaignId);

		return mongoTemplate.findOne(query, QuestDTO.class);
	}

	public void save(QuestDTO questDTO) {
		try {
			mongoTemplate.save(questDTO);
		} catch (RuntimeException e) {
			System.err.println(e.getMessage());

			throw new RuntimeException("Error saving campaign", e);
		}
	}

	public void updateMainQuest(QuestDTO questDTO) {
		try {
			Query query = buildMainQuestByCampaignIdQuery(questDTO.getCampaignId());
			Update update = buildUpdate(questDTO);

			QuestDTO updatedQuest = mongoTemplate.findAndModify(query, update, QuestDTO.class);

			if (updatedQuest == null) {
				throw new RuntimeException();
			}
		} catch (RuntimeException e) {
			System.err.println(e.getMessage());

			throw new RuntimeException("Error updating quest", e);
		}
	}

	private Update buildUpdate(QuestDTO questDTO) {
		return new Update()
			.set("title", questDTO.getTitle())
			.set("description", questDTO.getDescription())
			.set("goals", questDTO.getGoals());
	}

	private Query buildMainQuestByCampaignIdQuery(String campaignId) {
		return query(
			where("campaignId")
				.is(campaignId)
				.and("type")
				.is("main")
		);
	}
}
