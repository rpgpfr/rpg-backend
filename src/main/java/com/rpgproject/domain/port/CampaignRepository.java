package com.rpgproject.domain.port;

import com.rpgproject.domain.entity.Campaign;

import java.util.List;

public interface CampaignRepository {

	List<Campaign> getCampaignsByUserId(String userId);

	long getCountByUserId(String userId);

}
