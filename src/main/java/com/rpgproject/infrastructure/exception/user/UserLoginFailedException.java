package com.rpgproject.infrastructure.exception.user;

import com.rpgproject.domain.exception.UserException;

public class UserLoginFailedException extends UserException {

	public UserLoginFailedException() {
		super("Le mot de passe ou l'identifiant est incorrect.");
	}

}