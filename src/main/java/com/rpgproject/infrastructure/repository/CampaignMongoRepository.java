package com.rpgproject.infrastructure.repository;

import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.exception.campaign.CampaignCreationFailedException;
import com.rpgproject.domain.exception.campaign.CampaignNotFoundException;
import com.rpgproject.domain.exception.campaign.CampaignUpdateFailedException;
import com.rpgproject.domain.port.CampaignRepository;
import com.rpgproject.infrastructure.dao.CampaignMongoDao;
import com.rpgproject.infrastructure.dto.CampaignDTO;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class CampaignMongoRepository implements CampaignRepository {

	private final CampaignMongoDao campaignMongoDao;

	public CampaignMongoRepository(CampaignMongoDao campaignMongoDao) {
		this.campaignMongoDao = campaignMongoDao;
	}

	@Override
	public List<Campaign> getCampaignsByOwner(String owner) {
		List<CampaignDTO> campaignDTOs = campaignMongoDao.findAllCampaignsByOwner(owner);

		return mapToCampaigns(campaignDTOs);
	}

	@Override
	public Campaign getCampaignBySlugAndOwner(String slug, String owner) {
		CampaignDTO campaignDTO = campaignMongoDao.findCampaignBySlugAndOwner(slug, owner);

		if (campaignDTO == null) {
			throw new CampaignNotFoundException();
		}

		return mapToCampaign(campaignDTO);
	}

	private List<Campaign> mapToCampaigns(List<CampaignDTO> campaignDTOs) {
		return campaignDTOs
			.stream()
			.map(this::mapToCampaign)
			.toList();
	}

	private Campaign mapToCampaign(CampaignDTO campaignDTO) {
		return new Campaign(
			campaignDTO.getOwner(),
			campaignDTO.getName(),
			campaignDTO.getSlug(),
			campaignDTO.getDescription(),
			campaignDTO.getType(),
			campaignDTO.getMood(),
			null,
			campaignDTO.getCreatedAt()
		);
	}

	@Override
	public long getCountByOwner(String owner) {
		return campaignMongoDao.getCountByOwner(owner);
	}

	@Override
	public void save(Campaign campaign) {
		try {
			CampaignDTO campaignDTO = mapToCampaignDTO(campaign, LocalDate.now());
			campaignMongoDao.save(campaignDTO);
		} catch (RuntimeException e) {
			throw new CampaignCreationFailedException();
		}
	}

	@Override
	public void update(Campaign campaign, String slug) {
		try {
			CampaignDTO campaignDTO = mapToCampaignDTO(campaign);
			campaignMongoDao.update(campaignDTO, slug);
		} catch (RuntimeException e) {
			throw new CampaignUpdateFailedException();
		}
	}

	@Override
	public void delete(String slug, String owner) {
		try {
			CampaignDTO campaignDTO = campaignMongoDao.findCampaignBySlugAndOwner(slug, owner);

			campaignMongoDao.delete(campaignDTO);
		} catch (RuntimeException e) {
			throw new CampaignNotFoundException();
		}
	}

	private CampaignDTO mapToCampaignDTO(Campaign campaign) {
		return new CampaignDTO(
			campaign.getOwner(),
			campaign.getName(),
			campaign.getSlug(),
			campaign.getDescription(),
			campaign.getType(),
			campaign.getMood(),
			null
		);
	}

	private CampaignDTO mapToCampaignDTO(Campaign campaign, LocalDate createdAt) {
		return new CampaignDTO(
			campaign.getOwner(),
			campaign.getName(),
			campaign.getSlug(),
			campaign.getDescription(),
			campaign.getType(),
			campaign.getMood(),
			createdAt
		);
	}

}
