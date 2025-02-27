package com.rpgproject.application.presenter;

import com.rpgproject.application.dto.responsebody.Response;
import com.rpgproject.application.dto.viewmodel.UserViewModel;
import com.rpgproject.domain.entity.User;
import com.rpgproject.domain.port.Presenter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class UserRestPresenter implements Presenter<User, ResponseEntity<Response<UserViewModel>>> {

	@Override
	public ResponseEntity<Response<UserViewModel>> ok() {
		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<Response<UserViewModel>> ok(User user) {
		return null;
	}

	@Override
	public ResponseEntity<Response<UserViewModel>> error(Exception exception) {
		return ResponseEntity
			.badRequest()
			.body(
				new Response<>(null, exception.getMessage())
			);
	}

}
