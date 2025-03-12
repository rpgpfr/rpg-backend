package com.rpgproject.application.dto.viewmodel;

import com.fasterxml.jackson.annotation.JsonInclude;

public record UserViewModel(
	String username,
	String email,
	String firstName,
	String lastName,
	@JsonInclude(JsonInclude.Include.NON_NULL) String description,
	@JsonInclude(JsonInclude.Include.NON_NULL) String rpgKnowledge
) {

}
