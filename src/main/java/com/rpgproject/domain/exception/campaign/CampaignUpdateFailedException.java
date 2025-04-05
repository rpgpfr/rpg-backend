package com.rpgproject.domain.exception.campaign;

public class CampaignUpdateFailedException extends RuntimeException {

	public CampaignUpdateFailedException() {
		super("Une erreur est survenue lors de la mise à jour de la campagne");
	}

}
