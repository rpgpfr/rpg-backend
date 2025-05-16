package com.rpgproject.application.controller;

import com.rpgproject.application.annotation.CurrentOwner;
import com.rpgproject.application.dto.requestbody.CharacterRequestBody;
import com.rpgproject.application.dto.requestbody.CharacterUpdateRequestBody;
import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.CharacterViewModel;
import com.rpgproject.application.presenter.CharactersRestPresenter;
import com.rpgproject.domain.entity.Character;
import com.rpgproject.domain.port.CharacterRepository;
import com.rpgproject.domain.usecase.character.CreateCharacter;
import com.rpgproject.domain.usecase.character.DeleteCharacter;
import com.rpgproject.domain.usecase.character.GetCharactersBySlugAndOwner;
import com.rpgproject.domain.usecase.character.UpdateCharacter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/campaigns/{slug}/characters")
public class CharacterController {

	private final GetCharactersBySlugAndOwner<ResponseEntity<ResponseViewModel<List<CharacterViewModel>>>> getCharactersBySlugAndOwner;
	private final CreateCharacter<ResponseEntity<ResponseViewModel<List<CharacterViewModel>>>> createCharacter;
	private final UpdateCharacter<ResponseEntity<ResponseViewModel<List<CharacterViewModel>>>> updateCharacter;
	private final DeleteCharacter<ResponseEntity<ResponseViewModel<List<CharacterViewModel>>>> deleteCharacter;

	public CharacterController(CharacterRepository characterRepository, CharactersRestPresenter charactersRestPresenter) {
		this.getCharactersBySlugAndOwner = new GetCharactersBySlugAndOwner<>(characterRepository, charactersRestPresenter);
		this.createCharacter = new CreateCharacter<>(characterRepository, charactersRestPresenter);
		this.updateCharacter = new UpdateCharacter<>(characterRepository, charactersRestPresenter);
		this.deleteCharacter = new DeleteCharacter<>(characterRepository, charactersRestPresenter);
	}

	@GetMapping("")
	public ResponseEntity<ResponseViewModel<List<CharacterViewModel>>> getCharacters(@CurrentOwner String owner, @PathVariable String slug) {
		return getCharactersBySlugAndOwner.execute(slug, owner);
	}

	@PostMapping("")
	public ResponseEntity<ResponseViewModel<List<CharacterViewModel>>> createCharacter(@CurrentOwner String owner, @PathVariable String slug, @RequestBody CharacterRequestBody characterRequestBody) {
		Character character = new Character(owner, slug, characterRequestBody.name());

		return createCharacter.execute(character);
	}

	@PatchMapping("")
	public ResponseEntity<ResponseViewModel<List<CharacterViewModel>>> updateCharacter(@CurrentOwner String owner, @PathVariable String slug, @RequestBody CharacterUpdateRequestBody characterUpdateRequestBody) {
		Character character = new Character(owner, slug, characterUpdateRequestBody.newName());

		return updateCharacter.execute(character, characterUpdateRequestBody.oldName());
	}

	@DeleteMapping("")
	public ResponseEntity<ResponseViewModel<List<CharacterViewModel>>> deleteCharacter(@CurrentOwner String owner, @PathVariable String slug, @RequestBody CharacterRequestBody characterRequestBody) {
		Character character = new Character(owner, slug, characterRequestBody.name());

		return deleteCharacter.execute(character);
	}

}
