package com.rpgproject.domain.exception.user;

public class UserLoginFailedException extends RuntimeException {

	public UserLoginFailedException() {
		super("Le mot de passe ou l'identifiant est incorrect.");
	}

}