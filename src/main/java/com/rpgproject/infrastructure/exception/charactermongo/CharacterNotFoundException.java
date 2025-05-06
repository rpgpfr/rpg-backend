package com.rpgproject.infrastructure.exception.charactermongo;

public class CharacterNotFoundException extends RuntimeException {

	public CharacterNotFoundException() {
		super("Impossible de trouver votre personnage.");
	}

}
