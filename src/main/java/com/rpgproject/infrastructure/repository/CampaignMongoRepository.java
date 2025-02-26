package com.rpgproject.infrastructure.repository;

import com.rpgproject.domain.entity.Campaign;
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
	public List<Campaign> getCampaignsByUserId(String userId) {
		List<CampaignDTO> campaignDTOs = campaignMongoDao.findAllCampaignsByUserId(userId);

		return mapToCampaigns(campaignDTOs);
	}

	private List<Campaign> mapToCampaigns(List<CampaignDTO> campaignDTOs) {
		return campaignDTOs
			.stream()
			.map(dto -> new Campaign(dto.getName()))
			.toList();
	}

}
