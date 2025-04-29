package com.rpgproject.infrastructure.exception.questmongo;

public class QuestNotFoundException extends RuntimeException {
	public QuestNotFoundException() {
		super("Nous n'avons pas réussi à trouver votre quête.");
	}
}
