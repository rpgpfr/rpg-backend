package com.rpgproject.domain.entity;

import java.util.List;

import static java.util.Collections.emptyList;

public record Quest(String title, String type, String description, List<Goal> goals) {

	public static Quest createDefaultMainQuest() {
		return new Quest("", "main", "", emptyList());
	}

}
