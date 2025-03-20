package com.rpgproject.domain.entity;

import java.time.LocalDate;

public record User(String username, String email, String firstName, String lastName, String password,
				   String description,
				   String rpgKnowledge, LocalDate signedUpAt) {

	public User(String username, String email, String firstName, String lastName, String password) {
		this(username, email, firstName, lastName, password, null, null, null);
	}

}
