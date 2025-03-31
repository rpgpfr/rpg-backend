package com.rpgproject.domain.exception.quest;

public class CannotFindMainQuestException extends RuntimeException {
	public CannotFindMainQuestException() {
		super("Nous n'avons trouvé aucune quête principale pour cette campagne.");
	}
}
