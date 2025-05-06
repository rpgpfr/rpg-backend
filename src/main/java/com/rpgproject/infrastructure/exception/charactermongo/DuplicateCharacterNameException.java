package com.rpgproject.infrastructure.exception.charactermongo;

public class DuplicateCharacterNameException extends RuntimeException {

	public DuplicateCharacterNameException() {
		super("Le nom de ce personnage est déjà utilisé.");
	}

}
