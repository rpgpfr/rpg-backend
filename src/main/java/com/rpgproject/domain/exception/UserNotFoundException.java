package com.rpgproject.domain.exception;

public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException() {
		super("L'utilisateur n'existe pas");
	}

}
