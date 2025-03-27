package com.rpgproject.infrastructure.repository;

import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.exception.CampaignCreationFailedException;
import com.rpgproject.domain.exception.CampaignUpdateFailedException;
import com.rpgproject.domain.port.CampaignRepository;
import com.rpgproject.infrastructure.dao.CampaignMongoDao;
import com.rpgproject.infrastructure.dto.CampaignDTO;
import org.springframework.stereotype.Repository;

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

	private List<Campaign> mapToCampaigns(List<CampaignDTO> campaignDTOs) {
		return campaignDTOs
			.stream()
			.map(dto -> new Campaign(
				dto.getOwner(),
				dto.getName(),
				dto.getSlug(),
				dto.getDescription(),
				dto.getType(),
				dto.getMood()
			))
			.toList();
	}

	@Override
	public long getCountByOwner(String owner) {
		return campaignMongoDao.getCountByOwner(owner);
	}

	@Override
	public void save(Campaign campaign) {
		try {
			CampaignDTO campaignDTO = mapToCampaignDTO(campaign);
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

	private CampaignDTO mapToCampaignDTO(Campaign campaign) {
		return new CampaignDTO(
			campaign.owner(),
			campaign.name(),
			campaign.slug(),
			campaign.description(),
			campaign.type(),
			campaign.mood()
		);
	}

}
