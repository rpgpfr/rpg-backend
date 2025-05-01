package com.rpgproject.infrastructure.exception.campaignmongo;

public class CampaignNotFoundException extends RuntimeException {

  public CampaignNotFoundException() {
    super("Nous n'avons pas réussi à trouver votre campagne.");
  }

}
