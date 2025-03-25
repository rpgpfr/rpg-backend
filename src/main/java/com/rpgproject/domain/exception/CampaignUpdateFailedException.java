package com.rpgproject.domain.exception;

public class CampaignUpdateFailedException extends RuntimeException {

	public CampaignUpdateFailedException() {
		super("Une erreur est survenue lors de la mise à jour de la campagne");
	}

}
