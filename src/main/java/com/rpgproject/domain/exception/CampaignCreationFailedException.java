package com.rpgproject.domain.exception;

public class CampaignCreationFailedException extends RuntimeException {

	public CampaignCreationFailedException() {
		super("Une erreur est survenue lors de la création de la campagne");
	}

}
