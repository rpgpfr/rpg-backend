package com.rpgproject.infrastructure.repository;

import com.rpgproject.domain.entity.Goal;
import com.rpgproject.domain.entity.Quest;
import com.rpgproject.domain.exception.quest.CannotFindMainQuestException;
import com.rpgproject.domain.exception.quest.QuestEditFailedException;
import com.rpgproject.infrastructure.dao.CampaignMongoDao;
import com.rpgproject.infrastructure.dao.QuestMongoDao;
import com.rpgproject.infrastructure.dto.GoalDTO;
import com.rpgproject.infrastructure.dto.QuestDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QuestMongoRepository {

	private final QuestMongoDao questMongoDao;
	private final CampaignMongoDao campaignMongoDao;

	public QuestMongoRepository(QuestMongoDao questMongoDao, CampaignMongoDao campaignMongoDao) {
		this.questMongoDao = questMongoDao;
		this.campaignMongoDao = campaignMongoDao;
	}

	public Quest findMainQuestBySlugAndOwner(String slug, String owner) {
		try {
			String campaignId = campaignMongoDao.findCampaignIdBySlugAndOwner(slug, owner);
			QuestDTO questDTO = questMongoDao.findMainQuestByCampaignId(campaignId);

			return mapToQuest(questDTO);
		} catch (RuntimeException e) {
			throw new CannotFindMainQuestException();
		}
	}

	private Quest mapToQuest(QuestDTO questDTO) {
		return new Quest(
			questDTO.getTitle(),
			questDTO.getType(),
			questDTO.getDescription(),
			mapToGoals(questDTO.getGoals())
		);
	}

	private List<Goal> mapToGoals(List<GoalDTO> goalDTOs) {
		return goalDTOs
			.stream()
			.map(goalDTO -> new Goal(
				goalDTO.getName(),
				goalDTO.isCompleted()
			))
			.toList();
	}

	public void editMainQuest(Quest quest, String slug, String owner) {
		try {
			String campaignId = campaignMongoDao.findCampaignIdBySlugAndOwner(slug, owner);
			QuestDTO questDTO = mapToQuestDTO(quest, campaignId);

			questMongoDao.editMainQuest(questDTO);
		} catch (RuntimeException e) {
			throw new QuestEditFailedException();
		}
	}

	private QuestDTO mapToQuestDTO(Quest quest, String campaignId) {
		return new QuestDTO(
			campaignId,
			quest.type(),
			quest.title(),
			quest.description(),
			mapToGoalsDTO(quest.goals())
		);
	}

	private List<GoalDTO> mapToGoalsDTO(List<Goal> goals) {
		return goals
			.stream()
			.map(goal -> new GoalDTO(
				goal.name(),
				goal.completed()
			))
			.toList();
	}

}
