package com.rpgproject.infrastructure.repository;

import com.rpgproject.domain.entity.Campaign;
import com.rpgproject.domain.exception.DuplicateException;
import com.rpgproject.domain.exception.InternalException;
import com.rpgproject.domain.exception.NotFoundException;
import com.rpgproject.domain.port.CampaignRepository;
import com.rpgproject.infrastructure.dao.CampaignMongoDao;
import com.rpgproject.infrastructure.dto.CampaignDTO;
import com.rpgproject.infrastructure.exception.campaignmongo.CampaignNotFoundException;
import com.rpgproject.infrastructure.exception.campaignmongo.DuplicateCampaignNameException;
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
		try {
			CampaignDTO campaignDTO = campaignMongoDao.findCampaignBySlugAndOwner(slug, owner);

			return mapToCampaign(campaignDTO);
		} catch (CampaignNotFoundException e) {
			throw new NotFoundException(e.getMessage());
		}
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
		} catch (DuplicateCampaignNameException e) {
			throw new DuplicateException(e.getMessage());
		} catch (RuntimeException e) {
			throw new InternalException(e.getMessage());
		}
	}

	@Override
	public void update(Campaign campaign, String slug) {
		try {
			CampaignDTO campaignDTO = mapToCampaignDTO(campaign);
			campaignMongoDao.update(campaignDTO, slug);
		} catch (CampaignNotFoundException e) {
			throw new NotFoundException(e.getMessage());
		} catch (RuntimeException e) {
			throw new InternalException(e.getMessage());
		}
	}

	@Override
	public void delete(String slug, String owner) {
		try {
			CampaignDTO campaignDTO = campaignMongoDao.findCampaignBySlugAndOwner(slug, owner);

			campaignMongoDao.delete(campaignDTO);
		} catch (CampaignNotFoundException e) {
			throw new NotFoundException(e.getMessage());
		} catch (RuntimeException e) {
			throw new InternalException(e.getMessage());
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
