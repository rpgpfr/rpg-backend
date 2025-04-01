package com.rpgproject.domain.exception.campaign;

public class CampaignNotFoundException extends RuntimeException {

	public CampaignNotFoundException() {
		super("Nous n'avons pas trouvé votre campagne.");
	}

}
