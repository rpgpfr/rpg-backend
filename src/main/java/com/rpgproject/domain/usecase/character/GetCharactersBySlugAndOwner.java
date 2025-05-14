package com.rpgproject.domain.usecase.character;

import com.rpgproject.domain.entity.Character;
import com.rpgproject.domain.port.CharacterRepository;
import com.rpgproject.domain.port.Presenter;

import java.util.List;

public class GetCharactersBySlugAndOwner<T> {

	private final CharacterRepository characterRepository;
	private final Presenter<List<Character>, T> presenter;

	public GetCharactersBySlugAndOwner(CharacterRepository characterRepository, Presenter<List<Character>, T> presenter) {
		this.characterRepository = characterRepository;
		this.presenter = presenter;
	}


	public T execute(String campaignSlug, String owner) {
		List<Character> characters = characterRepository.getCharactersByCampaignsSlugAndOwner(campaignSlug, owner);

		return presenter.ok(characters);
	}

}
