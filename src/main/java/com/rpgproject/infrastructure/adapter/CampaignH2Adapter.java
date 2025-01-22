package com.rpgproject.infrastructure.adapter;

import com.rpgproject.domain.bean.Campaign;
import com.rpgproject.domain.exception.CannotCreateCampaignException;
import com.rpgproject.domain.port.CampaignRepository;
import com.rpgproject.infrastructure.dao.CampaignDao;
import com.rpgproject.infrastructure.dto.CampaignDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CampaignH2Adapter implements CampaignRepository {

	private final CampaignDao campaignDao;

	@Autowired
	public CampaignH2Adapter(CampaignDao campaignDao) {
		this.campaignDao = campaignDao;
	}

	@Override
	public List<Campaign> getAllCampaigns() {
		List<CampaignDTO> campaignDTOs = campaignDao.getAllCampaigns();

		return mapToCampaigns(campaignDTOs);
	}

	@Override
	public Campaign getCampaignByNameAndUsername(String name, String username) {
		CampaignDTO campaignDTO = campaignDao.getCampaignByNameAndUsername(name, username);

		return mapToCampaign(campaignDTO);
	}

	@Override
	public void insertCampaign(Campaign campaign) throws CannotCreateCampaignException {
		CampaignDTO campaignDTO = new CampaignDTO(campaign);

		try {
			campaignDao.insertCampaign(campaignDTO);
		} catch (DataIntegrityViolationException | NullPointerException e) {
			throw new CannotCreateCampaignException();
		}
	}

	private List<Campaign> mapToCampaigns(List<CampaignDTO> campaignDTOs) {
		return campaignDTOs
			.stream()
			.map(this::mapToCampaign)
			.toList();
	}

	private Campaign mapToCampaign(CampaignDTO campaignDTO) {
		return new Campaign(campaignDTO.getName(), campaignDTO.getUsername());
	}

}
