package com.rpgproject.domain.usecase.character;

import com.rpgproject.domain.entity.Character;
import com.rpgproject.domain.exception.InternalException;
import com.rpgproject.domain.exception.NotFoundException;
import com.rpgproject.domain.port.CharacterRepository;
import com.rpgproject.domain.port.Presenter;

import java.util.List;

public class DeleteCharacter<T> {

	CharacterRepository characterRepository;
	Presenter<List<Character>, T> characterPresenter;

	public DeleteCharacter(CharacterRepository characterRepository, Presenter<List<Character>, T> characterPresenter) {
		this.characterRepository = characterRepository;
		this.characterPresenter = characterPresenter;
	}

	public T execute(Character character) {
		try {
			characterRepository.delete(character);

			return characterPresenter.ok();
		} catch (NotFoundException | InternalException e) {
			return characterPresenter.error(e);
		}
	}

}
