package com.rpgproject.domain.usecase;

import com.rpgproject.domain.exception.CannotRegisterUserException;
import com.rpgproject.domain.port.UserPresenter;
import com.rpgproject.domain.port.UserRepository;

public class RegisterUser<T> {

	private final UserRepository userRepository;
	private final UserPresenter<T> userPresenter;

	public RegisterUser(UserRepository userRepository, UserPresenter<T> userPresenter) {
		this.userRepository = userRepository;
		this.userPresenter = userPresenter;
	}

	public T execute(String username) {
		try {
			userRepository.register(username);
			return userPresenter.ok();
		} catch (CannotRegisterUserException e) {
			return userPresenter.error(e);
		}
	}
}
