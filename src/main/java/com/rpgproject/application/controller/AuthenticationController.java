package com.rpgproject.application.controller;

import com.rpgproject.application.dto.requestbody.RegisterRequestBody;
import com.rpgproject.application.presenter.UserRestPresenter;
import com.rpgproject.domain.entity.User;
import com.rpgproject.domain.port.UserRepository;
import com.rpgproject.domain.usecase.RegisterUser;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AuthenticationController {

	final RegisterUser<ResponseEntity<String>> registerUser;

	public AuthenticationController(UserRepository userRepository, UserRestPresenter userRestPresenter) {
		registerUser = new RegisterUser<>(userRepository, userRestPresenter);
	}

	@PostMapping("/register")
	@CrossOrigin(origins = "*")
	public ResponseEntity<String> registerUser(@RequestBody RegisterRequestBody requestBody) {
		User user = new User(requestBody.getUser(), requestBody.getUsername());
		return registerUser.execute(user);
	}

}
