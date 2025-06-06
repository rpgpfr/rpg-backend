package com.rpgproject.infrastructure.dao;

import com.rpgproject.infrastructure.dto.QuestDTO;
import com.rpgproject.infrastructure.exception.questmongo.DuplicateQuestNameException;
import com.rpgproject.infrastructure.exception.questmongo.QuestNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Slf4j
@Component
public class QuestMongoDao {

	private final MongoTemplate mongoTemplate;

	public QuestMongoDao(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	public QuestDTO findMainQuestByCampaignSlugAndOwner(String campaignSlug, String owner) {
		Query query = buildMainQuestByCampaignSlugAndOwner(campaignSlug, owner);

		QuestDTO questDTO = mongoTemplate.findOne(query, QuestDTO.class);

		if (questDTO == null) {
			throw new QuestNotFoundException();
		}

		return questDTO;
	}

	public void save(QuestDTO questDTO) {
		try {
			mongoTemplate.insert(questDTO);
		} catch (DuplicateKeyException e) {
			log.error(e.getMessage());

			throw new DuplicateQuestNameException();
		} catch (RuntimeException e) {
			log.error(e.getMessage());

			throw new RuntimeException("Une erreur est survenue lors de la création de la quête.");
		}
	}

	public void updateMainQuest(QuestDTO questDTO) {
		try {
			updateMainQuestToDatabase(questDTO);
		} catch (QuestNotFoundException e) {
			log.error(e.getMessage());

			throw new QuestNotFoundException();
		} catch (RuntimeException e) {
			log.error(e.getMessage());

			throw new RuntimeException("Une erreur est survenue lors de la mise à jour des informations.", e);
		}
	}

	private void updateMainQuestToDatabase(QuestDTO questDTO) {
		Query query = buildMainQuestByCampaignSlugAndOwner(questDTO.getCampaignSlug(), questDTO.getOwner());
		Update update = buildUpdate(questDTO);

		QuestDTO updatedQuest = mongoTemplate.findAndModify(query, update, QuestDTO.class);

		if (updatedQuest == null) {
			throw new QuestNotFoundException();
		}
	}

	private Query buildMainQuestByCampaignSlugAndOwner(String campaignSlug, String owner) {
		return query(
			where("campaignSlug")
				.is(campaignSlug)
				.and("owner")
				.is(owner)
				.and("type")
				.is("main")
		);
	}

	private Update buildUpdate(QuestDTO questDTO) {
		return new Update()
			.set("title", questDTO.getTitle())
			.set("description", questDTO.getDescription())
			.set("goals", questDTO.getGoals());
	}

	public void deleteAllByCampaignSlugAndOwner(String campaignSlug, String owner) {
		Query query = buildQuestByCampaignSlugAndOwnerQuery(campaignSlug, owner);

		mongoTemplate.remove(query, QuestDTO.class);
	}

	private Query buildQuestByCampaignSlugAndOwnerQuery(String campaignSlug, String owner) {
		return query(
			where("campaignSlug")
				.is(campaignSlug)
				.and("owner")
				.is(owner)
		);
	}
}
