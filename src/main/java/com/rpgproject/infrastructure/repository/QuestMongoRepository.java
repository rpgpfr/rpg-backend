package com.rpgproject.infrastructure.repository;

import com.rpgproject.domain.entity.Goal;
import com.rpgproject.domain.entity.Quest;
import com.rpgproject.domain.exception.DuplicateException;
import com.rpgproject.domain.exception.InternalException;
import com.rpgproject.domain.exception.NotFoundException;
import com.rpgproject.domain.port.QuestRepository;
import com.rpgproject.infrastructure.dao.CampaignMongoDao;
import com.rpgproject.infrastructure.dao.QuestMongoDao;
import com.rpgproject.infrastructure.dto.GoalDTO;
import com.rpgproject.infrastructure.dto.QuestDTO;
import com.rpgproject.infrastructure.exception.campaignmongo.CampaignNotFoundException;
import com.rpgproject.infrastructure.exception.questmongo.DuplicateQuestNameException;
import com.rpgproject.infrastructure.exception.questmongo.QuestNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QuestMongoRepository implements QuestRepository {

	private final QuestMongoDao questMongoDao;
	private final CampaignMongoDao campaignMongoDao;

	public QuestMongoRepository(QuestMongoDao questMongoDao, CampaignMongoDao campaignMongoDao) {
		this.questMongoDao = questMongoDao;
		this.campaignMongoDao = campaignMongoDao;
	}

	@Override
	public Quest findMainQuestBySlugAndOwner(String slug, String owner) {
		try {
			String campaignId = campaignMongoDao.findCampaignIdBySlugAndOwner(slug, owner);
			QuestDTO questDTO = questMongoDao.findMainQuestByCampaignId(campaignId);

			return mapToQuest(questDTO);
		} catch (QuestNotFoundException | CampaignNotFoundException e) {
			throw new NotFoundException(e.getMessage());
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

	@Override
	public void save(Quest quest, String slug, String owner) {
		try {
			String campaignId = campaignMongoDao.findCampaignIdBySlugAndOwner(slug, owner);
			QuestDTO questDTO = mapToQuestDTO(quest, campaignId);

			questMongoDao.save(questDTO);
		} catch (DuplicateQuestNameException e) {
			throw new DuplicateException(e.getMessage());
		} catch (RuntimeException e) {
			throw new InternalException(e.getMessage());
		}
	}

	@Override
	public void updateMainQuest(Quest quest, String slug, String owner) {
		try {
			String campaignId = campaignMongoDao.findCampaignIdBySlugAndOwner(slug, owner);
			QuestDTO questDTO = mapToQuestDTO(quest, campaignId);

			questMongoDao.updateMainQuest(questDTO);
		} catch (QuestNotFoundException | CampaignNotFoundException e) {
			throw new NotFoundException(e.getMessage());
		} catch (RuntimeException e) {
			throw new InternalException(e.getMessage());
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

	@Override
	public void deleteBySlugAndOwner(String slug, String owner) {
		String campaignId = campaignMongoDao.findCampaignIdBySlugAndOwner(slug, owner);

		questMongoDao.deleteByCampaignId(campaignId);
	}

}
