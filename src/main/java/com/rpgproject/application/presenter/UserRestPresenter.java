package com.rpgproject.application.presenter;

import com.rpgproject.domain.port.UserPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class UserRestPresenter implements UserPresenter<ResponseEntity<String>> {

	@Override
	public ResponseEntity<String> ok() {
		return ResponseEntity.ok().build();
	}

	@Override
	public ResponseEntity<String> error(Exception exception) {
		return ResponseEntity.internalServerError().body(exception.getMessage());
	}

}
