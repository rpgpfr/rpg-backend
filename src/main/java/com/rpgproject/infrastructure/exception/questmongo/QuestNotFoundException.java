package com.rpgproject.infrastructure.exception.questmongo;

public class QuestNotFoundException extends RuntimeException {
	public QuestNotFoundException() {
		super("Impossible de trouver votre quête.");
	}
}
