package com.rpgproject.domain.port;

import com.rpgproject.domain.entity.Campaign;

import java.util.List;

public interface CampaignRepository {

	List<Campaign> getCampaignsByOwner(String userId);

	long getCountByOwner(String userId);

}
