package com.rpgproject.application.controller;

import com.rpgproject.application.dto.requestbody.RegisterRequestBody;
import com.rpgproject.application.presenter.UserRestPresenter;
import com.rpgproject.domain.port.UserRepository;
import com.rpgproject.domain.usecase.RegisterUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AuthenticationController {

	RegisterUser<ResponseEntity<String>> registerUser;

	@Autowired
	public AuthenticationController(UserRepository userRepository, UserRestPresenter userRestPresenter) {
		registerUser = new RegisterUser<>(userRepository, userRestPresenter);
	}

	@GetMapping("/register")
	public ResponseEntity<String> registerUser(HttpServletRequest request, @RequestBody RegisterRequestBody requestBody) {
		return registerUser.execute(requestBody.getUser());
	}


}
