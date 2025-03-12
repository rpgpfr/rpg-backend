package com.rpgproject.domain.exception;

public class UserLoginFailedException extends RuntimeException {

	public UserLoginFailedException() {
		super("Le mot de passe ou l'identifiant est incorrect.");
	}

}