package com.rpgproject.infrastructure.exception.userjdbc;

public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException() {
		super("L'utilisateur n'a pas été trouvé.");
	}

}
