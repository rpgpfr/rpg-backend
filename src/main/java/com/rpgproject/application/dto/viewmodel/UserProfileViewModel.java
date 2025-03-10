package com.rpgproject.application.dto.viewmodel;

public record UserProfileViewModel(String username, String email, String firstName, String lastName,
								   String description,
								   String rpgKnowledge, long campaignCount, long mapCount, long characterCount,
								   long resourceCount) {

}
