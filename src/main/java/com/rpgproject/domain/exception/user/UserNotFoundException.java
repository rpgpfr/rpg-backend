package com.rpgproject.domain.exception.user;

public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException() {
		super("L'utilisateur n'existe pas");
	}

}
