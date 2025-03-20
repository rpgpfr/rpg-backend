package com.rpgproject.application.controller;

import com.rpgproject.application.dto.requestbody.LoginRequestBody;
import com.rpgproject.application.dto.requestbody.RegisterRequestBody;
import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.UserViewModel;
import com.rpgproject.application.presenter.UserRestPresenter;
import com.rpgproject.domain.entity.User;
import com.rpgproject.domain.port.UserRepository;
import com.rpgproject.domain.usecase.user.LogUserIn;
import com.rpgproject.domain.usecase.user.RegisterUser;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {

	private final RegisterUser<ResponseEntity<ResponseViewModel<UserViewModel>>> registerUser;
	private final LogUserIn<ResponseEntity<ResponseViewModel<UserViewModel>>> logUserIn;

	public AuthenticationController(UserRepository userRepository, UserRestPresenter userRestPresenter) {
		registerUser = new RegisterUser<>(userRepository, userRestPresenter);
		logUserIn = new LogUserIn<>(userRepository, userRestPresenter);
	}

	@PostMapping("/register")
	@CrossOrigin(origins = "*")
	public @ResponseBody ResponseEntity<ResponseViewModel<UserViewModel>> registerUser(@RequestBody RegisterRequestBody requestBody) {
		User user = new User(requestBody.username(), requestBody.email(), requestBody.firstName(), requestBody.lastName(), requestBody.password());

		return registerUser.execute(user);
	}

	@PostMapping("/login")
	@CrossOrigin(origins = "*")
	public @ResponseBody ResponseEntity<ResponseViewModel<UserViewModel>> login(@RequestBody LoginRequestBody requestBody) {
		return logUserIn.execute(requestBody.identifier(), requestBody.password());
	}

}
