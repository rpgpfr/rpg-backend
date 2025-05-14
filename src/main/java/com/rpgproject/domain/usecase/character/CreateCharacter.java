package com.rpgproject.domain.usecase.character;

import com.rpgproject.domain.entity.Character;
import com.rpgproject.domain.exception.DuplicateException;
import com.rpgproject.domain.exception.InternalException;
import com.rpgproject.domain.port.CharacterRepository;
import com.rpgproject.domain.port.Presenter;

import java.util.List;

public class CreateCharacter<T> {

	private final CharacterRepository characterRepository;
	private final Presenter<List<Character>, T> characterPresenter;

	public CreateCharacter(CharacterRepository characterRepository, Presenter<List<Character>, T> characterPresenter) {
		this.characterRepository = characterRepository;
		this.characterPresenter = characterPresenter;
	}

	public T execute(Character character) {
		try {
			characterRepository.save(character);

			return characterPresenter.ok();
		} catch (DuplicateException | InternalException e) {
			return characterPresenter.error(e);
		}
	}

}
