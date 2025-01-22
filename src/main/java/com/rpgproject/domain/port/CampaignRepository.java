package com.rpgproject.domain.port;

import com.rpgproject.domain.bean.Campaign;
import com.rpgproject.domain.exception.CannotCreateCampaignException;

import java.util.List;

public interface CampaignRepository {

	List<Campaign> getAllCampaigns();

	Campaign getCampaignByNameAndUsername(String name, String username);

	void insertCampaign(Campaign campaign) throws CannotCreateCampaignException;

}
