package com.rpgproject.application.presenter;

import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.CharacterViewModel;
import com.rpgproject.application.service.ExceptionHTTPStatusService;
import com.rpgproject.domain.entity.Character;
import com.rpgproject.domain.port.Presenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CharacterRestPresenter implements Presenter<Character, ResponseEntity<ResponseViewModel<CharacterViewModel>>> {

	private final ExceptionHTTPStatusService exceptionHTTPStatusService;

	@Autowired
	public CharacterRestPresenter(ExceptionHTTPStatusService exceptionHTTPStatusService) {
		this.exceptionHTTPStatusService = exceptionHTTPStatusService;
	}

	@Override
	public ResponseEntity<ResponseViewModel<CharacterViewModel>> ok() {
		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<ResponseViewModel<CharacterViewModel>> ok(Character character) {
		return ResponseEntity.ok(
			new ResponseViewModel<>(
				new CharacterViewModel(character.name()),
				null
			)
		);
	}

	@Override
	public ResponseEntity<ResponseViewModel<CharacterViewModel>> error(RuntimeException exception) {
		return new ResponseEntity<>(
			new ResponseViewModel<>(
				null,
				exception.getMessage()
			),
			exceptionHTTPStatusService.getHttpStatusFromExceptionClass(exception)
		);
	}
}
