package com.rpgproject.infrastructure.exception.campaignmongo;

public class CampaignNotFoundException extends RuntimeException {

	public CampaignNotFoundException() {
		super("Impossible de trouver votre campagne.");
	}

}
