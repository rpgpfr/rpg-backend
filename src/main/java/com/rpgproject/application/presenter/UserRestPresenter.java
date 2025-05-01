package com.rpgproject.application.presenter;

import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.UserViewModel;
import com.rpgproject.application.service.ExceptionHTTPStatusService;
import com.rpgproject.domain.entity.User;
import com.rpgproject.domain.port.Presenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class UserRestPresenter implements Presenter<User, ResponseEntity<ResponseViewModel<UserViewModel>>> {

	private final ExceptionHTTPStatusService exceptionHTTPStatusService;

	@Autowired
	public UserRestPresenter(ExceptionHTTPStatusService exceptionHTTPStatusService) {
		this.exceptionHTTPStatusService = exceptionHTTPStatusService;
	}

	@Override
	public ResponseEntity<ResponseViewModel<UserViewModel>> ok() {
		return ResponseEntity.ok().build();
	}

	@Override
	public ResponseEntity<ResponseViewModel<UserViewModel>> ok(User user) {
		return ResponseEntity.ok(
			new ResponseViewModel<>(
				new UserViewModel(
					user.username(),
					user.email(),
					user.firstName(),
					user.lastName(),
					user.description(),
					user.rpgKnowledge()
				),
				null
			)
		);
	}

	@Override
	public ResponseEntity<ResponseViewModel<UserViewModel>> error(RuntimeException exception) {
		return new ResponseEntity<>(
			new ResponseViewModel<>(
				null,
				exception.getMessage()
			),
			exceptionHTTPStatusService.getHttpStatusFromExceptionClass(exception)
		);
	}

}
