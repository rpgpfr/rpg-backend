package com.rpgproject.domain.exception;

public class CannotCreateCampaignException extends Exception {

	public CannotCreateCampaignException() {
		super("We were not able to create your campaign due to technical issue");
	}

}
