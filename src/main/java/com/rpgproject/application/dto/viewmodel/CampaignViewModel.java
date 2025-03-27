package com.rpgproject.application.dto.viewmodel;

import com.fasterxml.jackson.annotation.JsonInclude;

public record CampaignViewModel(
	@JsonInclude(JsonInclude.Include.NON_NULL) String name,
	@JsonInclude(JsonInclude.Include.NON_NULL) String slug,
	@JsonInclude(JsonInclude.Include.NON_NULL) String description,
	@JsonInclude(JsonInclude.Include.NON_NULL) String type,
	@JsonInclude(JsonInclude.Include.NON_NULL) String mood) {

	public CampaignViewModel(String name, String slug) {
		this(name, slug, null, null, null);
	}

}
