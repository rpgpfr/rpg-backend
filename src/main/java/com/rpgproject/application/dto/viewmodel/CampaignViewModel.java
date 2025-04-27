package com.rpgproject.application.dto.viewmodel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rpgproject.application.dto.viewmodel.campaign.InfoViewModel;

import java.time.LocalDate;

public record CampaignViewModel(
	String name,
	String slug,
	@JsonInclude(JsonInclude.Include.NON_NULL) InfoViewModel info,
	@JsonInclude(JsonInclude.Include.NON_NULL) QuestViewModel mainQuest,
	LocalDate createdAt
) {

}
