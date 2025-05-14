package com.rpgproject.application.presenter;

import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.CharacterViewModel;
import com.rpgproject.application.service.ExceptionHTTPStatusService;
import com.rpgproject.domain.entity.Character;
import com.rpgproject.domain.port.Presenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CharactersRestPresenter implements Presenter<List<Character>, ResponseEntity<ResponseViewModel<List<CharacterViewModel>>>> {

	private final ExceptionHTTPStatusService exceptionHTTPStatusService;

	@Autowired
	public CharactersRestPresenter(ExceptionHTTPStatusService exceptionHTTPStatusService) {
		this.exceptionHTTPStatusService = exceptionHTTPStatusService;
	}

	@Override
	public ResponseEntity<ResponseViewModel<List<CharacterViewModel>>> ok() {
		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<ResponseViewModel<List<CharacterViewModel>>> ok(List<Character> characters) {
		return ResponseEntity.ok(
			new ResponseViewModel<>(
				characters
					.stream()
					.map(character -> new CharacterViewModel(character.name()))
					.toList()
				,
				null
			)
		);
	}

	@Override
	public ResponseEntity<ResponseViewModel<List<CharacterViewModel>>> error(RuntimeException exception) {
		return new ResponseEntity<>(
			new ResponseViewModel<>(
				null,
				exception.getMessage()
			),
			exceptionHTTPStatusService.getHttpStatusFromExceptionClass(exception)
		);
	}
}
