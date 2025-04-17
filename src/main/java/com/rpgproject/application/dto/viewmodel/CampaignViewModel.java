package com.rpgproject.application.dto.viewmodel;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

public record CampaignViewModel(
	String name,
	String slug,
	@JsonInclude(JsonInclude.Include.NON_NULL) String description,
	@JsonInclude(JsonInclude.Include.NON_NULL) String type,
	@JsonInclude(JsonInclude.Include.NON_NULL) String mood,
	@JsonInclude(JsonInclude.Include.NON_NULL) QuestViewModel mainQuest,
	LocalDate createdAt
) {

}
