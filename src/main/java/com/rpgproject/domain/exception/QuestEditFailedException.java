package com.rpgproject.domain.exception;

public class QuestEditFailedException extends RuntimeException {

	public QuestEditFailedException() {
		super("Une erreur est survenue lors de l'édition de la quête.");
	}

}
