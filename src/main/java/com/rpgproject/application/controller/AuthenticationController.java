package com.rpgproject.application.controller;

import com.rpgproject.application.annotation.CurrentOwner;
import com.rpgproject.application.presenter.UserRestPresenter;
import com.rpgproject.domain.port.UserRepository;
import com.rpgproject.domain.usecase.RegisterUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {

	RegisterUser<ResponseEntity<String>> registerUser;

	@Autowired
	public AuthenticationController(UserRepository userRepository, UserRestPresenter userRestPresenter) {
		registerUser = new RegisterUser<>(userRepository, userRestPresenter);
	}

	@GetMapping("/register")
	public ResponseEntity<String> registerUser(@CurrentOwner String owner) {
		return registerUser.execute(owner);
	}


}
