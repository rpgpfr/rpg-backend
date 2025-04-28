package com.rpgproject.infrastructure.exception.campaignmongo;

public class DuplicateCampaignNameException extends RuntimeException {

	public DuplicateCampaignNameException() {
		super("Le nom de cette campagne est déjà utilisé.");
	}

}
