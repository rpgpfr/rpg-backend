package com.rpgproject.domain.exception;

public class CannotRegisterUserException extends RuntimeException {

	public CannotRegisterUserException() {
		super("An error occurred while registering the user");
	}

}
