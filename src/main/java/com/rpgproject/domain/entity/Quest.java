package com.rpgproject.domain.entity;

import java.util.List;

import static java.util.Collections.emptyList;

public record Quest(String owner, String campaignSlug, String title, String type, String description, List<Goal> goals) {

	public static Quest createDefaultMainQuest(String campaignSlug, String owner) {
		return new Quest(owner, campaignSlug, "", "main", "", emptyList());
	}

}
