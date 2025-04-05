package com.rpgproject.domain.exception.quest;

public class QuestEditFailedException extends RuntimeException {

	public QuestEditFailedException() {
		super("Une erreur est survenue lors de l'édition de la quête.");
	}

}
