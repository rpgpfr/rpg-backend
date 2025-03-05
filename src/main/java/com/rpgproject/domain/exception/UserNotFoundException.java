package com.rpgproject.domain.exception;

public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException() {
		super("User was not found");
	}

}
