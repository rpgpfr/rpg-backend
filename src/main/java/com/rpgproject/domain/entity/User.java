package com.rpgproject.domain.entity;

public record User(String uniqueName, String username, String firstName, String lastName, String description,
				   String rpgKnowledge) {

	public User(String uniqueName, String username) {
		this(uniqueName, username, null, null, null, null);
	}

}
