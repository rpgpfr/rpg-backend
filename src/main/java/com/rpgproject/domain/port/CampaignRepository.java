package com.rpgproject.domain.port;

import com.rpgproject.domain.entity.Campaign;

import java.util.List;

public interface CampaignRepository {

	List<Campaign> getCampaignsByOwner(String owner);

	long getCountByOwner(String owner);

	void save(Campaign campaign);

}
