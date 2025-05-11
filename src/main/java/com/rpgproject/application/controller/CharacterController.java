package com.rpgproject.application.controller;

import com.rpgproject.application.annotation.CurrentOwner;
import com.rpgproject.application.dto.requestbody.CharacterUpdateRequestBody;
import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.CharacterViewModel;
import com.rpgproject.application.presenter.CharacterRestPresenter;
import com.rpgproject.domain.entity.Character;
import com.rpgproject.domain.port.CharacterRepository;
import com.rpgproject.domain.usecase.character.CreateCharacter;
import com.rpgproject.domain.usecase.character.DeleteCharacter;
import com.rpgproject.domain.usecase.character.UpdateCharacter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/campaigns/{slug}/characters")
public class CharacterController {

	private final CreateCharacter<ResponseEntity<ResponseViewModel<CharacterViewModel>>> createCharacter;
	private final UpdateCharacter<ResponseEntity<ResponseViewModel<CharacterViewModel>>> updateCharacter;
	private final DeleteCharacter<ResponseEntity<ResponseViewModel<CharacterViewModel>>> deleteCharacter;

	public CharacterController(CharacterRepository characterRepository, CharacterRestPresenter characterRestPresenter) {
		this.createCharacter = new CreateCharacter<>(characterRepository, characterRestPresenter);
		this.updateCharacter = new UpdateCharacter<>(characterRepository, characterRestPresenter);
		this.deleteCharacter = new DeleteCharacter<>(characterRepository, characterRestPresenter);
	}

	@PostMapping("")
	public ResponseEntity<ResponseViewModel<CharacterViewModel>> createCharacter(@CurrentOwner String owner, @PathVariable String slug, @RequestParam String name) {
		Character character = new Character(owner, slug, name);

		return createCharacter.execute(character);
	}

	@PutMapping("")
	public ResponseEntity<ResponseViewModel<CharacterViewModel>> updateCharacter(@CurrentOwner String owner, @PathVariable String slug, @RequestBody CharacterUpdateRequestBody characterUpdateRequestBody) {
		Character character = new Character(owner, slug, characterUpdateRequestBody.newName());

		return updateCharacter.execute(character, characterUpdateRequestBody.oldName());
	}

	@DeleteMapping("")
	public ResponseEntity<ResponseViewModel<CharacterViewModel>> deleteCharacter(@CurrentOwner String owner, @PathVariable String slug, @RequestParam String name) {
		Character character = new Character(owner, slug, name);

		return deleteCharacter.execute(character);
	}

}
