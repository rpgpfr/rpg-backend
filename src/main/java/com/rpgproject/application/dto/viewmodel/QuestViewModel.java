package com.rpgproject.application.dto.viewmodel;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public record QuestViewModel(
	String title,
	String type,
	@JsonInclude(JsonInclude.Include.NON_NULL) String description,
	List<GoalViewModel> goals
) {

}
