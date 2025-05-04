package com.rpgproject.infrastructure.exception.questmongo;

public class DuplicateQuestNameException extends RuntimeException {

	public DuplicateQuestNameException() {
		super("Le nom de cette quête est déjà utilisé.");
	}

}
