package com.rpgproject.domain.exception.quest;

public class MainQuestNotFoundException extends RuntimeException {
	public MainQuestNotFoundException() {
		super("Nous n'avons trouvé aucune quête principale pour cette campagne.");
	}
}
