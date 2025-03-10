package com.rpgproject.domain.entity;

public record User(String username, String email, String firstName, String lastName, String password, String description,
				   String rpgKnowledge) {

	public User(String username, String email, String firstName, String lastName, String password) {
		this(username, email, firstName, lastName, password, null, null);
	}

}
