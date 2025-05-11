package com.rpgproject.domain.usecase.character;

import com.rpgproject.domain.entity.Character;
import com.rpgproject.domain.exception.DuplicateException;
import com.rpgproject.domain.exception.InternalException;
import com.rpgproject.domain.exception.NotFoundException;
import com.rpgproject.domain.port.CharacterRepository;
import com.rpgproject.domain.port.Presenter;

public class UpdateCharacter<T> {

	CharacterRepository characterRepository;
	Presenter<Character, T> characterPresenter;

	public UpdateCharacter(CharacterRepository characterRepository, Presenter<Character, T> characterPresenter) {
		this.characterRepository = characterRepository;
		this.characterPresenter = characterPresenter;
	}

	public T execute(Character character, String oldName) {
		try {
			characterRepository.update(character, oldName);

			return characterPresenter.ok();
		} catch (DuplicateException | NotFoundException | InternalException e) {
			return characterPresenter.error(e);
		}
	}

}
