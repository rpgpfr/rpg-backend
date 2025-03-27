package com.rpgproject.domain.entity;

public record Campaign(String owner, String name, String slug, String description, String type, String mood) {

	public Campaign(String owner, String name) {
		this(owner, name, null, null, null, null);
	}

}
