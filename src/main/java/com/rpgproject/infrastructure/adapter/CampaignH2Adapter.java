package com.rpgproject.infrastructure.adapter;

import com.rpgproject.domain.bean.Campaign;
import com.rpgproject.domain.exception.CannotCreateCampaignException;
import com.rpgproject.domain.exception.NoResultException;
import com.rpgproject.domain.port.repository.CampaignRepository;
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
	public Campaign getCampaignByNameAndUsername(String name, String username) {
		CampaignDTO campaignDTO = campaignDao.getCampaignByNameAndUsername(name, username);

		if (campaignDTO.getName() == null & campaignDTO.getUsername() == null) {
			throw new NoResultException();
		}

		return mapToCampaign(campaignDTO);
	}

	@Override
	public List<Campaign> getCampaignsByUsername(String username) {
		List<CampaignDTO> campaignDTOs = campaignDao.getCampaignsByUsername(username);

		return mapToCampaigns(campaignDTOs);
	}

	@Override
	public void createCampaign(Campaign campaign) {
		CampaignDTO campaignDTO = new CampaignDTO(campaign);

		try {
			campaignDao.createCampaign(campaignDTO);
		} catch (DataIntegrityViolationException e) {
			throw new CannotCreateCampaignException("La campagne existe déjà.");
		} catch (NullPointerException e) {
			throw new CannotCreateCampaignException("Des informations sont manquantes.");
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
