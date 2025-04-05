package com.rpgproject.domain.exception.quest;

public class QuestCreationException extends RuntimeException {

	public QuestCreationException() {
		super("Nous n'avons pas pu créer la quête.");
	}

}
