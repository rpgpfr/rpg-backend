package com.rpgproject.infrastructure.repository;

import com.rpgproject.domain.entity.Goal;
import com.rpgproject.domain.entity.Quest;
import com.rpgproject.domain.exception.DuplicateException;
import com.rpgproject.domain.exception.InternalException;
import com.rpgproject.domain.exception.NotFoundException;
import com.rpgproject.domain.port.QuestRepository;
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

	public QuestMongoRepository(QuestMongoDao questMongoDao) {
		this.questMongoDao = questMongoDao;
	}

	@Override
	public Quest findMainQuestByCampaignSlugAndOwner(String campaignSlug, String owner) {
		try {
			QuestDTO questDTO = questMongoDao.findMainQuestByCampaignSlugAndOwner(campaignSlug, owner);

			return mapToQuest(questDTO);
		} catch (QuestNotFoundException | CampaignNotFoundException e) {
			throw new NotFoundException(e.getMessage());
		}
	}

	private Quest mapToQuest(QuestDTO questDTO) {
		return new Quest(
			questDTO.getOwner(),
			questDTO.getCampaignSlug(),
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
	public void save(Quest quest) {
		try {
			QuestDTO questDTO = mapToQuestDTO(quest);

			questMongoDao.save(questDTO);
		} catch (DuplicateQuestNameException e) {
			throw new DuplicateException(e.getMessage());
		} catch (RuntimeException e) {
			throw new InternalException(e.getMessage());
		}
	}

	@Override
	public void updateMainQuest(Quest quest) {
		try {
			QuestDTO questDTO = mapToQuestDTO(quest);

			questMongoDao.updateMainQuest(questDTO);
		} catch (QuestNotFoundException | CampaignNotFoundException e) {
			throw new NotFoundException(e.getMessage());
		} catch (RuntimeException e) {
			throw new InternalException(e.getMessage());
		}
	}

	private QuestDTO mapToQuestDTO(Quest quest) {
		return new QuestDTO(
			quest.owner(),
			quest.campaignSlug(),
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
	public void deleteAllByCampaignSlugAndOwner(String campaignSlug, String owner) {
		questMongoDao.deleteAllByCampaignSlugAndOwner(campaignSlug, owner);
	}

}
