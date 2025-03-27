package com.rpgproject.domain.entity;

public record Campaign(String owner, String name, String slug, String description, String type, String mood) {

	public Campaign(String slug) {
		this(null, null, slug, null, null, null);
	}

	public Campaign(String owner, String name, String slug) {
		this(owner, name, slug, null, null, null);
	}

}
