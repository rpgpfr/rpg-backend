package com.rpgproject.application.controller;

import com.rpgproject.application.dto.requestbody.RegisterRequestBody;
import com.rpgproject.application.presenter.UserRestPresenter;
import com.rpgproject.domain.port.UserRepository;
import com.rpgproject.domain.usecase.RegisterUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AuthenticationController {

	RegisterUser<ResponseEntity<String>> registerUser;

	@Autowired
	public AuthenticationController(UserRepository userRepository, UserRestPresenter userRestPresenter) {
		registerUser = new RegisterUser<>(userRepository, userRestPresenter);
	}

	@PostMapping("/register")
	@CrossOrigin(origins = "*")
	public ResponseEntity<String> registerUser(@RequestBody RegisterRequestBody requestBody) {
		ResponseEntity<String> result = registerUser.execute(requestBody.getUser());
		return result;
	}


}
