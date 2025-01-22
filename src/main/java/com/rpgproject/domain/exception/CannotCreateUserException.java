package com.rpgproject.domain.exception;

public class CannotCreateUserException extends Exception {

	public CannotCreateUserException() {
		super("We were not able to create your account due to technical issue");
	}

}
