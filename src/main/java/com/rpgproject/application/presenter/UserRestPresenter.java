package com.rpgproject.application.presenter;

import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.UserViewModel;
import com.rpgproject.domain.entity.User;
import com.rpgproject.domain.port.Presenter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class UserRestPresenter implements Presenter<User, ResponseEntity<ResponseViewModel<UserViewModel>>> {

	@Override
	public ResponseEntity<ResponseViewModel<UserViewModel>> ok() {
		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<ResponseViewModel<UserViewModel>> ok(User user) {
		return null;
	}

	@Override
	public ResponseEntity<ResponseViewModel<UserViewModel>> error(Exception exception) {
		return ResponseEntity
			.badRequest()
			.body(
				new ResponseViewModel<>(null, exception.getMessage())
			);
	}

}
