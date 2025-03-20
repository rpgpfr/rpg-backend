package com.rpgproject.domain.exception;

public class UserUpdateFailedException extends RuntimeException {

	public UserUpdateFailedException() {
		super("Une erreur est survenue lors de la mise à jour de l'utilisateur");
	}

}
