package com.rpgproject.domain.port.repository;

import com.rpgproject.domain.bean.Campaign;
import com.rpgproject.domain.exception.CannotCreateCampaignException;

import java.util.List;

public interface CampaignRepository {

	Campaign getCampaignByNameAndUsername(String name, String username);

	List<Campaign> getCampaignsByUsername(String username);

	void createCampaign(Campaign campaign) throws CannotCreateCampaignException;

}
