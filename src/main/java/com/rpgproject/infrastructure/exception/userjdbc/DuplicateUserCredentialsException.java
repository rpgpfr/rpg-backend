package com.rpgproject.infrastructure.exception.userjdbc;

public class DuplicateUserCredentialsException extends RuntimeException {

	public DuplicateUserCredentialsException() {
		super("Le nom d'utilisateur ou l'email est déjà utilisé.");
	}

}
